package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CaregiverScheduleRepository implements ScheduleRepository {

    private static final Logger log = LoggerFactory.getLogger(CaregiverScheduleRepository.class);
    private final CaregiverRepositoryAdapter caregiverAdapter;
    private final WorkingScheduleRepository workingScheduleRepository;

    public CaregiverScheduleRepository(
            CaregiverRepositoryAdapter caregiverAdapter,
            WorkingScheduleRepository workingScheduleRepository) {
        this.caregiverAdapter = caregiverAdapter;
        this.workingScheduleRepository = workingScheduleRepository;
    }

    @Transactional
    @Override
    public boolean saveSchedule(String caregiverId, String schedule) {
        try {
            Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
            
            if (!optionalCaregiver.isPresent()) {
                log.error("Caregiver not found: {}", caregiverId);
                return false;
            }
            
            Caregiver caregiver = optionalCaregiver.get();
            
            // Check for duplicate schedule
            if (caregiver.getWorkingSchedule().contains(schedule)) {
                log.warn("Schedule already exists for caregiver {}: {}", caregiverId, schedule);
                return false;
            }
            
            // Parse the schedule
            String[] parts = schedule.split(" ");
            if (parts.length != 2) {
                log.warn("Invalid schedule format: {}", schedule);
                return false;
            }
            
            String day = parts[0];
            String[] timeRange = parts[1].split("-");
            
            if (timeRange.length != 2) {
                log.warn("Invalid time range format: {}", parts[1]);
                return false;
            }
            
            String startTime = timeRange[0];
            String endTime = timeRange[1];
            
            // Check consultation duration limit (2 hours)
            if (!isValidConsultationDuration(startTime, endTime)) {
                log.warn("Consultation duration exceeds limit: {}", schedule);
                return false;
            }
            
            // Check for overlapping schedules
            for (String existingSchedule : caregiver.getWorkingSchedule()) {
                String[] existingParts = existingSchedule.split(" ");
                String existingDay = existingParts[0];
                
                // Only check schedules on the same day
                if (existingDay.equals(day)) {
                    String[] existingTimeRange = existingParts[1].split("-");
                    String existingStartTime = existingTimeRange[0];
                    String existingEndTime = existingTimeRange[1];
                    
                    // Check for overlap
                    if ((startTime.compareTo(existingStartTime) >= 0 && startTime.compareTo(existingEndTime) < 0) ||
                        (endTime.compareTo(existingStartTime) > 0 && endTime.compareTo(existingEndTime) <= 0) ||
                        (startTime.compareTo(existingStartTime) <= 0 && endTime.compareTo(existingEndTime) >= 0)) {
                        log.warn("Schedule overlaps with existing schedule for caregiver {}: {} overlaps with {}", 
                                caregiverId, schedule, existingSchedule);
                        return false;
                    }
                }
            }
            
            WorkingSchedule workingSchedule = new WorkingSchedule();
            workingSchedule.setCaregiverId(caregiverId);
            workingSchedule.setSchedule(schedule);
            workingSchedule.setStatus("AVAILABLE");
            workingSchedule.setAvailable(true);
            workingScheduleRepository.save(workingSchedule);
            
            return true;
        } catch (Exception e) {
            log.error("Error saving schedule", e);
            return false;
        }
    }
    
    @Override
    public boolean isScheduleAvailable(String caregiverId, String schedule) {
        try {
            Optional<WorkingSchedule> optionalWorkingSchedule = 
                    workingScheduleRepository.findByCaregiverIdAndSchedule(caregiverId, schedule);
            
            return optionalWorkingSchedule.isPresent() && optionalWorkingSchedule.get().isAvailable();
        } catch (Exception e) {
            log.error("Error checking schedule availability", e);
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteSchedule(String caregiverId, String schedule) {
        try {
            Optional<WorkingSchedule> optionalSchedule = 
                    workingScheduleRepository.findByCaregiverIdAndSchedule(caregiverId, schedule);
            
            if (!optionalSchedule.isPresent()) {
                log.warn("Schedule not found: {}", schedule);
                return false;
            }
            
            WorkingSchedule workingSchedule = optionalSchedule.get();
            workingScheduleRepository.delete(workingSchedule);
            
            return true;
        } catch (Exception e) {
            log.error("Error deleting schedule", e);
            return false;
        }
    }

    @Transactional
    @Override
    public boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule) {
        try {
            // First check if the old schedule exists
            Optional<WorkingSchedule> optionalOldSchedule = 
                    workingScheduleRepository.findByCaregiverIdAndSchedule(caregiverId, oldSchedule);
            
            if (!optionalOldSchedule.isPresent()) {
                log.warn("Old schedule not found: {}", oldSchedule);
                return false;
            }
            
            // Then verify the new schedule format and check for overlaps
            String[] parts = newSchedule.split(" ");
            if (parts.length != 2) {
                log.warn("Invalid schedule format: {}", newSchedule);
                return false;
            }
            
            String day = parts[0];
            String[] timeRange = parts[1].split("-");
            
            if (timeRange.length != 2) {
                log.warn("Invalid time range format: {}", parts[1]);
                return false;
            }
            
            String startTime = timeRange[0];
            String endTime = timeRange[1];
            
            // Check consultation duration limit (2 hours)
            if (!isValidConsultationDuration(startTime, endTime)) {
                log.warn("Consultation duration exceeds limit: {}", newSchedule);
                return false;
            }
            
            // Check for overlaps with existing schedules
            Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
            if (!optionalCaregiver.isPresent()) {
                log.error("Caregiver not found: {}", caregiverId);
                return false;
            }
            
            Caregiver caregiver = optionalCaregiver.get();
            
            for (String existingSchedule : caregiver.getWorkingSchedule()) {
                if (existingSchedule.equals(oldSchedule)) {
                    continue; // Skip the schedule we're modifying
                }
                
                String[] existingParts = existingSchedule.split(" ");
                String existingDay = existingParts[0];
                
                // Only check schedules on the same day
                if (existingDay.equals(day)) {
                    String[] existingTimeRange = existingParts[1].split("-");
                    String existingStartTime = existingTimeRange[0];
                    String existingEndTime = existingTimeRange[1];
                    
                    // Check for overlap
                    if ((startTime.compareTo(existingStartTime) >= 0 && startTime.compareTo(existingEndTime) < 0) ||
                        (endTime.compareTo(existingStartTime) > 0 && endTime.compareTo(existingEndTime) <= 0) ||
                        (startTime.compareTo(existingStartTime) <= 0 && endTime.compareTo(existingEndTime) >= 0)) {
                        log.warn("New schedule overlaps with existing schedule: {} overlaps with {}", 
                                newSchedule, existingSchedule);
                        return false;
                    }
                }
            }
            
            // If all checks pass, update the schedule
            WorkingSchedule workingSchedule = optionalOldSchedule.get();
            workingSchedule.setSchedule(newSchedule);
            workingScheduleRepository.save(workingSchedule);
            
            return true;
        } catch (Exception e) {
            log.error("Error modifying schedule", e);
            return false;
        }
    }

    // Helper method to validate consultation duration
    private boolean isValidConsultationDuration(String startTime, String endTime) {
        try {
            String[] startParts = startTime.split(":");
            String[] endParts = endTime.split(":");
            
            int startHour = Integer.parseInt(startParts[0]);
            int startMinute = Integer.parseInt(startParts[1]);
            int endHour = Integer.parseInt(endParts[0]);
            int endMinute = Integer.parseInt(endParts[1]);
            
            // Calculate duration in minutes
            int durationMinutes = (endHour - startHour) * 60 + (endMinute - startMinute);
            
            // Check if duration is no more than 2 hours (120 minutes)
            return durationMinutes > 0 && durationMinutes <= 120;
        } catch (Exception e) {
            log.error("Error calculating consultation duration", e);
            return false;
        }
    }
    @Transactional
    @Override
    public boolean saveScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
            
            if (!optionalCaregiver.isPresent()) {
                log.error("Caregiver not found: {}", caregiverId);
                return false;
            }
            
            // Check if duration is valid (not more than 2 hours)
            long durationMinutes = Duration.between(startTime, endTime).toMinutes();
            if (durationMinutes <= 0 || durationMinutes > 120) {
                log.warn("Consultation duration exceeds limit: {} - {}", startTime, endTime);
                return false;
            }
            
            // Check for overlaps with existing schedules
            List<WorkingSchedule> existingSchedules = workingScheduleRepository.findByCaregiverId(caregiverId);
            for (WorkingSchedule existingSchedule : existingSchedules) {
                // Check for time overlap
                if (existingSchedule.getStartTime() != null && existingSchedule.getEndTime() != null) {
                    // Check if the schedules overlap
                    if ((startTime.isEqual(existingSchedule.getStartTime()) || startTime.isAfter(existingSchedule.getStartTime())) && 
                        startTime.isBefore(existingSchedule.getEndTime()) ||
                        (endTime.isAfter(existingSchedule.getStartTime()) && 
                        (endTime.isEqual(existingSchedule.getEndTime()) || endTime.isBefore(existingSchedule.getEndTime()))) ||
                        (startTime.isBefore(existingSchedule.getStartTime()) && endTime.isAfter(existingSchedule.getEndTime()))) {
                        log.warn("Schedule overlaps with existing schedule for caregiver {}: {} to {} overlaps with {} to {}", 
                                caregiverId, startTime, endTime, existingSchedule.getStartTime(), existingSchedule.getEndTime());
                        return false;
                    }
                }
            }
            
            // Create and save new schedule
            WorkingSchedule workingSchedule = new WorkingSchedule();
            workingSchedule.setCaregiverId(caregiverId);
            workingSchedule.setStartTime(startTime);
            workingSchedule.setEndTime(endTime);
            workingSchedule.setStatus("AVAILABLE");
            workingSchedule.setAvailable(true);
            workingScheduleRepository.save(workingSchedule);
            
            return true;
        } catch (Exception e) {
            log.error("Error saving schedule with date time", e);
            return false;
        }
    }

    @Override
    public boolean isScheduleAvailableByDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            Optional<WorkingSchedule> optionalWorkingSchedule = 
                    workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime(caregiverId, startTime, endTime);
            
            return optionalWorkingSchedule.isPresent() && optionalWorkingSchedule.get().isAvailable();
        } catch (Exception e) {
            log.error("Error checking schedule availability by date time", e);
            return false;
        }
    }
    @Transactional
    @Override
    public boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            Optional<WorkingSchedule> optionalSchedule = 
                    workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime(caregiverId, startTime, endTime);
            
            if (!optionalSchedule.isPresent()) {
                log.warn("Schedule not found: {} to {}", startTime, endTime);
                return false;
            }
            
            WorkingSchedule workingSchedule = optionalSchedule.get();
            workingScheduleRepository.delete(workingSchedule);
            
            return true;
        } catch (Exception e) {
            log.error("Error deleting schedule with date time", e);
            return false;
        }
    }

    @Transactional
    @Override
    public boolean modifyScheduleWithDateTime(String caregiverId, LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                        LocalDateTime newStartTime, LocalDateTime newEndTime) {
        try {
            // First check if the old schedule exists
            Optional<WorkingSchedule> optionalOldSchedule = 
                    workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime(caregiverId, oldStartTime, oldEndTime);
            
            if (!optionalOldSchedule.isPresent()) {
                log.warn("Old schedule not found: {} to {}", oldStartTime, oldEndTime);
                return false;
            }
            
            // Check if duration is valid (not more than 2 hours)
            long durationMinutes = Duration.between(newStartTime, newEndTime).toMinutes();
            if (durationMinutes <= 0 || durationMinutes > 120) {
                log.warn("Consultation duration exceeds limit: {} - {}", newStartTime, newEndTime);
                return false;
            }
            
            // Check for overlaps with existing schedules
            List<WorkingSchedule> existingSchedules = workingScheduleRepository.findByCaregiverId(caregiverId);
            for (WorkingSchedule existingSchedule : existingSchedules) {
                // Skip the schedule we're modifying
                if (existingSchedule.getStartTime() != null && existingSchedule.getEndTime() != null &&
                    existingSchedule.getStartTime().equals(oldStartTime) && existingSchedule.getEndTime().equals(oldEndTime)) {
                    continue;
                }
                
                // Check if the schedules overlap
                if (existingSchedule.getStartTime() != null && existingSchedule.getEndTime() != null) {
                    if ((newStartTime.isEqual(existingSchedule.getStartTime()) || newStartTime.isAfter(existingSchedule.getStartTime())) && 
                        newStartTime.isBefore(existingSchedule.getEndTime()) ||
                        (newEndTime.isAfter(existingSchedule.getStartTime()) && 
                        (newEndTime.isEqual(existingSchedule.getEndTime()) || newEndTime.isBefore(existingSchedule.getEndTime()))) ||
                        (newStartTime.isBefore(existingSchedule.getStartTime()) && newEndTime.isAfter(existingSchedule.getEndTime()))) {
                        log.warn("New schedule overlaps with existing schedule: {} to {} overlaps with {} to {}", 
                                newStartTime, newEndTime, existingSchedule.getStartTime(), existingSchedule.getEndTime());
                        return false;
                    }
                }
            }
            
            // If all checks pass, update the schedule
            WorkingSchedule workingSchedule = optionalOldSchedule.get();
            workingSchedule.setStartTime(newStartTime);
            workingSchedule.setEndTime(newEndTime);
            workingScheduleRepository.save(workingSchedule);
            
            return true;
        } catch (Exception e) {
            log.error("Error modifying schedule with date time", e);
            return false;
        }
    }

    @Override
    public List<String> getCaregiverSchedules(String caregiverId) {
        List<WorkingSchedule> workingSchedules = workingScheduleRepository.findByCaregiverId(caregiverId);
        List<String> scheduleStrings = new ArrayList<>();
        
        for (WorkingSchedule workingSchedule : workingSchedules) {
            if (workingSchedule.getSchedule() != null) {
                scheduleStrings.add(workingSchedule.getSchedule());
            } else if (workingSchedule.getStartTime() != null && workingSchedule.getEndTime() != null) {
                // Format DateTime schedules to String for backward compatibility
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedSchedule = workingSchedule.getStartTime().format(formatter) + "-" + 
                                        workingSchedule.getEndTime().format(formatter);
                scheduleStrings.add(formattedSchedule);
            }
        }
        
        return scheduleStrings;
    }
}