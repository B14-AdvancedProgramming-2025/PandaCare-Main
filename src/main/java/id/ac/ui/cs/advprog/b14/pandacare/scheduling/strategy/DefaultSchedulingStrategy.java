package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSchedulingStrategy implements SchedulingStrategy {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultSchedulingStrategy.class);
    private final ScheduleRepository scheduleRepository;
    private final ConsultationRepository consultationRepository;
    private final WorkingScheduleRepository workingScheduleRepository;
    
    public DefaultSchedulingStrategy(
        ScheduleRepository scheduleRepository, 
        ConsultationRepository consultationRepository,
        WorkingScheduleRepository workingScheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.consultationRepository = consultationRepository;
        this.workingScheduleRepository = workingScheduleRepository;
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
        
        Optional<WorkingSchedule> workingSchedule = workingScheduleRepository.findByCaregiverIdAndSchedule(caregiverId, schedule);
        if (workingSchedule.isPresent() && !workingSchedule.get().isAvailable()) {
            log.warn("Schedule is already booked: {}", schedule);
            return false;
        }
        
        return consultationRepository.saveConsultation(caregiverId, pacilianId, schedule, "BOOKED");
    }
    
    @Override
    public boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status) {
        log.info("Updating consultation status for caregiver {}, patient {}, schedule {}: {}",
                caregiverId, pacilianId, schedule, status);
        
        return consultationRepository.updateStatus(caregiverId, pacilianId, schedule, status);
    }

    @Override
    public List<Consultation> getCaregiverConsultations(String caregiverId) {
        log.info("Getting consultations for caregiver {}", caregiverId);
        return consultationRepository.findConsultationsByCaregiverId(caregiverId);
    }
    @Override
    public List<Consultation> getPatientConsultations(String pacilianId) {
        log.info("Getting consultations for patient {}", pacilianId);
        return consultationRepository.findConsultationsByPacilianId(pacilianId);
    }
}