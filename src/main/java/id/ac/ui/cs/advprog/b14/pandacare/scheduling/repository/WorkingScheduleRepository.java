package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {
    
    Optional<WorkingSchedule> findByCaregiverIdAndSchedule(String caregiverId, String schedule);
    
    List<WorkingSchedule> findByCaregiverId(String caregiverId);
    
    @Modifying
    @Transactional
    @Query("UPDATE WorkingSchedule w SET w.available = ?3, w.status = ?4 WHERE w.caregiverId = ?1 AND w.schedule = ?2")
    int updateAvailability(String caregiverId, String schedule, boolean available, String status);
}