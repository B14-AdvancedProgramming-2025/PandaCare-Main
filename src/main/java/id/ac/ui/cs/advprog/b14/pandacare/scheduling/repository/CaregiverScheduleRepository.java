package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}