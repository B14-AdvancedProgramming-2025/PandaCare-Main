package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DefaultSchedulingStrategy implements SchedulingStrategy {

    private static final Logger log = LoggerFactory.getLogger(DefaultSchedulingStrategy.class);
    private final ScheduleRepository scheduleRepository;
    private final ConsultationRepository consultationRepository;
    private final ConsultationService consultationService;
    
    public DefaultSchedulingStrategy(
            ScheduleRepository scheduleRepository, 
            ConsultationRepository consultationRepository,
            WorkingScheduleRepository workingScheduleRepository,
            ConsultationService consultationService) {
        this.scheduleRepository = scheduleRepository;
        this.consultationRepository = consultationRepository;
        this.consultationService = consultationService;
    }
    
    @Override
    public boolean createSchedule(String caregiverId, String schedule) {
        log.info("Creating schedule for caregiver {}: {}", caregiverId, schedule);
        return scheduleRepository.saveSchedule(caregiverId, schedule);
    }

    @Override
    public boolean bookConsultation(String caregiverId, String pacilianId, String schedule) {
        log.info("Booking consultation for caregiver {}, patient {}, schedule {}", 
                caregiverId, pacilianId, schedule);
        
        if (!scheduleRepository.isScheduleAvailable(caregiverId, schedule)) {
            log.warn("Schedule not available or does not exist: {}", schedule);
            return false;
        }
        
        return consultationService.saveConsultation(caregiverId, pacilianId, schedule, "PENDING");
    }

    @Override
    public boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status) {
        log.info("Updating consultation status for caregiver {}, patient {}, schedule {}: {}",
                caregiverId, pacilianId, schedule, status);
        
        return consultationService.updateStatus(caregiverId, pacilianId, schedule, status);
    }

    @Override
    public List<Consultation> getCaregiverConsultations(String caregiverId) {
        log.info("Getting consultations for caregiver {}", caregiverId);
        return consultationService.findConsultationsByCaregiverId(caregiverId);
    }

    @Override
    public List<Consultation> getPatientConsultations(String pacilianId) {
        log.info("Getting consultations for patient {}", pacilianId);
        return consultationService.findConsultationsByPacilianId(pacilianId);
    }

    @Override
    public boolean deleteSchedule(String caregiverId, String schedule) {
        log.info("Deleting schedule for caregiver {}: {}", caregiverId, schedule);
        
        // Find all consultations for this schedule
        List<Consultation> consultations = consultationRepository.findByCaregiverIdAndScheduleTime(
                caregiverId, schedule);
        
        for (Consultation consultation : consultations) {
            // Only allowed to delete if all consultations are in REJECTED status
            if (!"REJECTED".equals(consultation.getStatus())) {
                log.warn("Cannot delete schedule with non-rejected consultations: {}", schedule);
                return false;
            }
        }

        for (Consultation consultation : consultations) {
            consultationRepository.delete(consultation);
            log.info("Deleted rejected consultation for schedule: {}", schedule);
        }
        
        return scheduleRepository.deleteSchedule(caregiverId, schedule);
    }

    @Override
    public boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule) {
        log.info("Modifying schedule for caregiver {}: {} -> {}", 
                caregiverId, oldSchedule, newSchedule);
        
        // Find all consultations for this schedule
        List<Consultation> consultations = consultationRepository.findByCaregiverIdAndScheduleTime(
                caregiverId, oldSchedule);
        
        boolean hasModifiableConsultations = false;
        
        for (Consultation consultation : consultations) {
            // Only MODIFIED or REJECTED consultations allow schedule modification
            if (!"MODIFIED".equals(consultation.getStatus()) && !"REJECTED".equals(consultation.getStatus())) {
                log.warn("Cannot modify schedule with consultations in status {}: {}", 
                        consultation.getStatus(), oldSchedule);
                return false;
            }
            
            if ("MODIFIED".equals(consultation.getStatus())) {
                hasModifiableConsultations = true;
            }
        }
        
        boolean scheduleModified = scheduleRepository.modifySchedule(caregiverId, oldSchedule, newSchedule);
        
        if (!scheduleModified) {
            return false;
        }
        
        // Update consultations with MODIFIED status to use the new schedule and set to ACCEPTED
        if (hasModifiableConsultations) {
            for (Consultation consultation : consultations) {
                if ("MODIFIED".equals(consultation.getStatus())) {
                    consultation.setScheduleTime(newSchedule);
                    consultation.setStatus("ACCEPTED");
                    consultationRepository.save(consultation);
                    log.info("Updated consultation schedule time and set status to ACCEPTED: {}", 
                            consultation.getId());
                } else if ("REJECTED".equals(consultation.getStatus())) {
                    // Delete rejected consultations when modifying schedule
                    consultationRepository.delete(consultation);
                    log.info("Deleted rejected consultation when modifying schedule: {}", 
                            consultation.getId());
                }
            }
        }
        
        return true;
    }
    // New methods implementation
    @Override
    public boolean createScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Creating schedule for caregiver {}: {} to {}", 
                caregiverId, startTime, endTime);
        return scheduleRepository.saveScheduleWithDateTime(caregiverId, startTime, endTime);
    }
    
    @Override
    public boolean bookConsultationWithDateTime(String caregiverId, String pacilianId, 
                                             LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Booking consultation for caregiver {} with patient {}: {} to {}", 
                caregiverId, pacilianId, startTime, endTime);
        
        // Check if the schedule is available
        if (!scheduleRepository.isScheduleAvailableByDateTime(caregiverId, startTime, endTime)) {
            log.warn("Schedule is not available for booking: {} to {}", startTime, endTime);
            return false;
        }
        
        // Save the consultation
        return consultationService.saveConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime, "PENDING");
    }
    
    @Override
    public boolean updateConsultationStatusWithDateTime(String caregiverId, String pacilianId, 
                                                    LocalDateTime startTime, LocalDateTime endTime, String status) {
        log.info("Updating consultation status for caregiver {} with patient {}: {} to {} -> {}", 
                caregiverId, pacilianId, startTime, endTime, status);
        return consultationService.updateStatusWithDateTime(caregiverId, pacilianId, startTime, endTime, status);
    }
    
    @Override
    public boolean deleteScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Deleting schedule for caregiver {}: {} to {}", 
                caregiverId, startTime, endTime);
        return scheduleRepository.isScheduleAvailableByDateTime(caregiverId, startTime, endTime);
    }
    
    @Override
    public boolean modifyScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                        LocalDateTime newStartTime, LocalDateTime newEndTime) {
        log.info("Modifying schedule for caregiver {}: {} to {} -> {} to {}", 
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        // Find all consultations for this schedule
        List<Consultation> consultations = consultationRepository.findByCaregiverIdAndStartTimeBetween(
                caregiverId, oldStartTime, oldEndTime);
        
        boolean hasModifiableConsultations = false;
        
        for (Consultation consultation : consultations) {
            // Only MODIFIED or REJECTED consultations allow schedule modification
            if (!"MODIFIED".equals(consultation.getStatus()) && !"REJECTED".equals(consultation.getStatus())) {
                log.warn("Cannot modify schedule with consultations in status {}: {} to {}", 
                        consultation.getStatus(), oldStartTime, oldEndTime);
                return false;
            }
            
            if ("MODIFIED".equals(consultation.getStatus())) {
                hasModifiableConsultations = true;
            }
        }
        
        boolean scheduleModified = scheduleRepository.modifyScheduleWithDateTime(
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        if (!scheduleModified) {
            return false;
        }
        
        // Update consultations with MODIFIED status to use the new schedule and set to ACCEPTED
        if (hasModifiableConsultations) {
            for (Consultation consultation : consultations) {
                if ("MODIFIED".equals(consultation.getStatus())) {
                    consultation.setStartTime(newStartTime);
                    consultation.setEndTime(newEndTime);
                    consultation.setStatus("ACCEPTED");
                    consultationRepository.save(consultation);
                    log.info("Updated consultation schedule time and set status to ACCEPTED: {}", 
                            consultation.getId());
                }
            }
        }
        
        return true;
    }
}