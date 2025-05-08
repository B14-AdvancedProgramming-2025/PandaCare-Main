package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaWorkingScheduleRepository implements WorkingScheduleRepository {
    
    private static final Logger log = LoggerFactory.getLogger(JpaWorkingScheduleRepository.class);
    private final WorkingScheduleJpaRepository jpaRepository;
    
    public JpaWorkingScheduleRepository(WorkingScheduleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<WorkingSchedule> findByCaregiverIdAndSchedule(String caregiverId, String schedule) {
        try {
            return jpaRepository.findByCaregiverIdAndSchedule(caregiverId, schedule);
        } catch (Exception e) {
            log.error("Error finding schedule for caregiver {} and time {}", caregiverId, schedule, e);
            return Optional.empty();
        }
    }
    
    @Override
    public List<WorkingSchedule> findByCaregiverId(String caregiverId) {
        try {
            return jpaRepository.findByCaregiverId(caregiverId);
        } catch (Exception e) {
            log.error("Error finding schedules for caregiver {}", caregiverId, e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean updateAvailability(String caregiverId, String schedule, boolean available, String status) {
        try {
            int updatedRows = jpaRepository.updateAvailability(caregiverId, schedule, available, status);
            return updatedRows > 0;
        } catch (Exception e) {
            log.error("Error updating availability for caregiver {} and schedule {}", caregiverId, schedule, e);
            return false;
        }
    }
    
    @Override
    public WorkingSchedule save(WorkingSchedule workingSchedule) {
        try {
            return jpaRepository.save(workingSchedule);
        } catch (Exception e) {
            log.error("Error saving working schedule", e);
            return null;
        }
    }
}