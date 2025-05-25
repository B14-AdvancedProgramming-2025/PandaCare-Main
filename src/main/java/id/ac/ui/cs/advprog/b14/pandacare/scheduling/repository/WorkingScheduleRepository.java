package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, String> {
    List<WorkingSchedule> findByCaregiverId(String caregiverId);

    List<WorkingSchedule> findByCaregiverIdAndStartTimeBetween(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    Optional<WorkingSchedule> findByCaregiverIdAndStartTimeAndEndTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    @Modifying
    @Query("UPDATE WorkingSchedule w SET w.available = ?4, w.status = ?5 WHERE w.caregiverId = ?1 AND w.startTime = ?2 AND w.endTime = ?3")
    int updateAvailabilityByDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime, boolean available, String status);

    @Query("SELECT w FROM WorkingSchedule w WHERE w.caregiverId = :caregiverId " +
        "AND w.startTime <= :endTime AND w.endTime >= :startTime")
    List<WorkingSchedule> findByCaregiversWithOverlappingTimeSlots(
            @Param("caregiverId") String caregiverId, 
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT w FROM WorkingSchedule w WHERE w.caregiverId = :caregiverId " +
        "AND CAST(w.startTime AS LocalDate) = :date " +
        "AND w.available = :available")
    List<WorkingSchedule> findByCaregiverIdAndDateAndAvailable(
            @Param("caregiverId") String caregiverId,
            @Param("date") LocalDate date,
            @Param("available") boolean available);
}