package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JpaConsultationRepository implements ConsultationRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaConsultationRepository.class);
    private final ConsultationJpaRepository jpaRepository;
    private final WorkingScheduleRepository workingScheduleRepository;
    
    public JpaConsultationRepository(
            ConsultationJpaRepository jpaRepository,
            WorkingScheduleRepository workingScheduleRepository) {
        this.jpaRepository = jpaRepository;
        this.workingScheduleRepository = workingScheduleRepository;
    }
    
    @Override
    public boolean saveConsultation(String caregiverId, String pacilianId, String scheduleTime, String status) {
        try {
            // Generate a unique ID for the consultation
            String consultationId = UUID.randomUUID().toString();
            
            // Create and save the consultation
            Consultation consultation = new Consultation(consultationId, caregiverId, pacilianId, scheduleTime, status);
            jpaRepository.save(consultation);
            
            // Update the schedule availability
            updateScheduleAvailability(caregiverId, scheduleTime, false, "BOOKED");
            
            return true;
        } catch (Exception e) {
            log.error("Error saving consultation", e);
            return false;
        }
    }

    // Updated method to use WorkingScheduleRepository
    private boolean updateScheduleAvailability(String caregiverId, String scheduleTime, boolean isAvailable, String status) {
        try {
            return workingScheduleRepository.updateAvailability(caregiverId, scheduleTime, isAvailable, status);
        } catch (Exception e) {
            log.error("Error updating schedule availability", e);
            return false;
        }
    }
    
    @Override
    public boolean updateStatus(String caregiverId, String pacilianId, String schedule, String status) {
        try {
            Consultation consultation = jpaRepository.findByCaregiverIdAndPacilianIdAndScheduleTime(
                    caregiverId, pacilianId, schedule);
            
            if (consultation == null) {
                log.error("Consultation not found");
                return false;
            }
            
            consultation.setStatus(status);
            jpaRepository.save(consultation);
            
            // If the consultation is rejected, make the schedule available again
            if ("REJECTED".equals(status)) {
                updateScheduleAvailability(caregiverId, schedule, true, "AVAILABLE");
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error updating consultation status", e);
            return false;
        }
    }

    @Override
    public List<Consultation> findConsultationsByCaregiverId(String caregiverId) {
        try {
            return jpaRepository.findByCaregiverId(caregiverId);
        } catch (Exception e) {
            log.error("Error finding consultations for caregiver {}", caregiverId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Consultation> findConsultationsByPacilianId(String pacilianId) {
        try {
            return jpaRepository.findByPacilianId(pacilianId);
        } catch (Exception e) {
            log.error("Error finding consultations for patient {}", pacilianId, e);
            return new ArrayList<>();
        }
    }
}