package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaConsultationRepository implements ConsultationRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaConsultationRepository.class);
    private final ConsultationJpaRepository jpaRepository;
    
    public JpaConsultationRepository(ConsultationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public boolean saveConsultation(String caregiverId, String pacilianId, String schedule, String status) {
        try {
            String consultationId = UUID.randomUUID().toString();
            Consultation consultation = new Consultation(consultationId, caregiverId, pacilianId, schedule, status);
            jpaRepository.save(consultation);
            return true;
        } catch (Exception e) {
            log.error("Error saving consultation", e);
            return false;
        }
    }
    
    @Override
    public boolean updateStatus(String caregiverId, String pacilianId, String schedule, String status) {
        try {
            Consultation consultation = jpaRepository.findByCaregiver_IdAndPacilian_IdAndScheduleTime(
                    caregiverId, pacilianId, schedule);
            
            if (consultation == null) {
                log.error("Consultation not found");
                return false;
            }
            
            consultation.setStatus(status);
            jpaRepository.save(consultation);
            return true;
        } catch (Exception e) {
            log.error("Error updating consultation status", e);
            return false;
        }
    }
}