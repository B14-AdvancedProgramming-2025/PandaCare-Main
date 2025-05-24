package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, String> {
    
    List<Consultation> findByCaregiverId(String caregiverId);
    
    List<Consultation> findByPacilianId(String pacilianId);

    List<Consultation> findByCaregiverIdAndStartTimeBetween(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    List<Consultation> findByPacilianIdAndStartTimeBetween(String pacilianId, LocalDateTime startTime, LocalDateTime endTime);
    
    Consultation findByCaregiverIdAndPacilianIdAndStartTimeAndEndTime(
        String caregiverId, String pacilianId, LocalDateTime startTime, LocalDateTime endTime);
}