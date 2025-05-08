package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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
            
            // Parse the new schedule
            String[] parts = schedule.split(" ");
            String day = parts[0];
            String[] timeRange = parts[1].split("-");
            String startTime = timeRange[0];
            String endTime = timeRange[1];
            
            // Check for overlapping schedules
            for (String existingSchedule : caregiver.getWorkingSchedule()) {
                String[] existingParts = existingSchedule.split(" ");
                String existingDay = existingParts[0];
                
                // Only check schedules on the same day
                if (existingDay.equals(day)) {
                    String[] existingTimeRange = existingParts[1].split("-");
                    String existingStartTime = existingTimeRange[0];
                    String existingEndTime = existingTimeRange[1];
                    
                    // Check for overlap (simple time string comparison works if format is consistent HH:MM)
                    if ((startTime.compareTo(existingStartTime) >= 0 && startTime.compareTo(existingEndTime) < 0) ||
                        (endTime.compareTo(existingStartTime) > 0 && endTime.compareTo(existingEndTime) <= 0) ||
                        (startTime.compareTo(existingStartTime) <= 0 && endTime.compareTo(existingEndTime) >= 0)) {
                        log.warn("Schedule overlaps with existing schedule for caregiver {}: {} overlaps with {}", 
                                caregiverId, schedule, existingSchedule);
                        return false;
                    }
                }
            }
            
            caregiver.getWorkingSchedule().add(schedule);
            caregiverAdapter.save(caregiver);
            
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
            Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
            
            if (!optionalCaregiver.isPresent()) {
                log.error("Caregiver not found: {}", caregiverId);
                return false;
            }
            
            Caregiver caregiver = optionalCaregiver.get();
            return caregiver.getWorkingSchedule().contains(schedule);
        } catch (Exception e) {
            log.error("Error checking schedule availability", e);
            return false;
        }
    }
}