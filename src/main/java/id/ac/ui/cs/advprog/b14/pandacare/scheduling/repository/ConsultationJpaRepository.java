package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationJpaRepository extends JpaRepository<Consultation, String> {
    
    Consultation findByCaregiverIdAndPacilianIdAndScheduleTime(
            String caregiverId, String pacilianId, String scheduleTime);
}