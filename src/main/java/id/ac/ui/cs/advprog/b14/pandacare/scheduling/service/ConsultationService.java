package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsultationService {

    private static final Logger log = LoggerFactory.getLogger(ConsultationService.class);
    private final ConsultationRepository consultationRepository;
    private final WorkingScheduleRepository workingScheduleRepository;
    
    public ConsultationService(ConsultationRepository consultationRepository, 
                            WorkingScheduleRepository workingScheduleRepository) {
        this.consultationRepository = consultationRepository;
        this.workingScheduleRepository = workingScheduleRepository;
    }
    
    // Legacy method for backward compatibility
    @Transactional
    public boolean saveConsultation(String caregiverId, String pacilianId, String scheduleTime, String status) {
        try {
            String consultationId = UUID.randomUUID().toString();
            
            Consultation consultation = new Consultation(consultationId, caregiverId, pacilianId, scheduleTime, status);
            consultationRepository.save(consultation);
            
            updateScheduleAvailability(caregiverId, scheduleTime, false, "BOOKED");
            
            return true;
        } catch (Exception e) {
            log.error("Error saving consultation", e);
            return false;
        }
    }
    
    // New method using DateTime
    @Transactional
    public boolean saveConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime, String status) {
        try {
            String consultationId = UUID.randomUUID().toString();
            
            Consultation consultation = new Consultation(consultationId, caregiverId, pacilianId, startTime, endTime, status);
            consultationRepository.save(consultation);
            
            updateScheduleAvailabilityByDateTime(caregiverId, startTime, endTime, false, "BOOKED");
            
            return true;
        } catch (Exception e) {
            log.error("Error saving consultation with date time", e);
            return false;
        }
    }
    
    // Legacy method for backward compatibility
    @Transactional
    public boolean updateStatus(String caregiverId, String pacilianId, String schedule, String status) {
        try {
            Consultation consultation = consultationRepository
                    .findByCaregiverIdAndPacilianIdAndScheduleTime(caregiverId, pacilianId, schedule);
            
            if (consultation == null) {
                log.error("Consultation not found");
                return false;
            }
            
            consultation.setStatus(status);
            consultationRepository.save(consultation);

            if ("REJECTED".equals(status)) {
                updateScheduleAvailability(caregiverId, schedule, true, "AVAILABLE");
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error updating consultation status", e);
            return false;
        }
    }
    
    // New method using DateTime
    @Transactional
    public boolean updateStatusWithDateTime(String caregiverId, String pacilianId, 
                                        LocalDateTime startTime, LocalDateTime endTime, String status) {
        try {
            Consultation consultation = consultationRepository
                    .findByCaregiverIdAndPacilianIdAndStartTimeAndEndTime(caregiverId, pacilianId, startTime, endTime);
            
            if (consultation == null) {
                log.error("Consultation not found");
                return false;
            }
            
            consultation.setStatus(status);
            consultationRepository.save(consultation);

            if ("REJECTED".equals(status)) {
                updateScheduleAvailabilityByDateTime(caregiverId, startTime, endTime, true, "AVAILABLE");
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error updating consultation status with date time", e);
            return false;
        }
    }
    
    // Legacy method for backward compatibility
    private boolean updateScheduleAvailability(String caregiverId, String scheduleTime, boolean isAvailable, String status) {
        try {
            int updatedRows = workingScheduleRepository.updateAvailability(
                    caregiverId, scheduleTime, isAvailable, status);
            return updatedRows > 0;
        } catch (Exception e) {
            log.error("Error updating schedule availability", e);
            return false;
        }
    }
    
    // New method using DateTime
    private boolean updateScheduleAvailabilityByDateTime(String caregiverId, LocalDateTime startTime, 
                                                    LocalDateTime endTime, boolean isAvailable, String status) {
        try {
            int updatedRows = workingScheduleRepository.updateAvailabilityByDateTime(
                    caregiverId, startTime, endTime, isAvailable, status);
            return updatedRows > 0;
        } catch (Exception e) {
            log.error("Error updating schedule availability by date time", e);
            return false;
        }
    }
    
    public List<Consultation> findConsultationsByCaregiverId(String caregiverId) {
        return consultationRepository.findByCaregiverId(caregiverId);
    }
    
    public List<Consultation> findConsultationsByPacilianId(String pacilianId) {
        return consultationRepository.findByPacilianId(pacilianId);
    }

    @Transactional
    public boolean updateConsultationSchedule(String consultationId, String newScheduleTime, String newStatus) {
        try {
            Optional<Consultation> optConsultation = consultationRepository.findById(consultationId);
            
            if (!optConsultation.isPresent()) {
                log.error("Consultation not found: {}", consultationId);
                return false;
            }
            
            Consultation consultation = optConsultation.get();
            consultation.setScheduleTime(newScheduleTime);
            consultation.setStatus(newStatus);
            consultationRepository.save(consultation);
            
            return true;
        } catch (Exception e) {
            log.error("Error updating consultation schedule time", e);
            return false;
        }
    }

    @Transactional
    public boolean deleteConsultation(String consultationId) {
        try {
            Optional<Consultation> optConsultation = consultationRepository.findById(consultationId);
            
            if (!optConsultation.isPresent()) {
                log.error("Consultation not found: {}", consultationId);
                return false;
            }
            
            consultationRepository.delete(optConsultation.get());
            return true;
        } catch (Exception e) {
            log.error("Error deleting consultation", e);
            return false;
        }
    }
}