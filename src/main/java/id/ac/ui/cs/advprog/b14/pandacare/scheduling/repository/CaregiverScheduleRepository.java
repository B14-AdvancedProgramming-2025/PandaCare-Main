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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            workingSchedule.setId(UUID.randomUUID().toString());
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
            List<WorkingSchedule> overlappingSchedules = workingScheduleRepository.findByCaregiversWithOverlappingTimeSlots(
                    caregiverId, startTime, endTime);

            // If any overlapping schedule is found and available, return true
            return overlappingSchedules.stream()
                    .anyMatch(schedule -> schedule.isAvailable() && "AVAILABLE".equals(schedule.getStatus()));
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
            if (workingSchedule.getStartTime() != null && workingSchedule.getEndTime() != null) {
                scheduleStrings.add(workingSchedule.getFormattedSchedule());
            }
        }

        return scheduleStrings;
    }
}