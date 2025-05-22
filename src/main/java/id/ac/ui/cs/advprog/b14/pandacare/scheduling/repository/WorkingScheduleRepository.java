package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    
    // Legacy methods for backward compatibility
    Optional<WorkingSchedule> findByCaregiverIdAndSchedule(String caregiverId, String schedule);
    
    List<WorkingSchedule> findByCaregiverId(String caregiverId);
    
    @Modifying
    @Query("UPDATE WorkingSchedule w SET w.available = ?3, w.status = ?4 WHERE w.caregiverId = ?1 AND w.schedule = ?2")
    int updateAvailability(String caregiverId, String schedule, boolean available, String status);
    
    // New methods using DateTime
    List<WorkingSchedule> findByCaregiverIdAndStartTimeBetween(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    Optional<WorkingSchedule> findByCaregiverIdAndStartTimeAndEndTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Modifying
    @Query("UPDATE WorkingSchedule w SET w.available = ?4, w.status = ?5 WHERE w.caregiverId = ?1 AND w.startTime = ?2 AND w.endTime = ?3")
    int updateAvailabilityByDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime, boolean available, String status);
}