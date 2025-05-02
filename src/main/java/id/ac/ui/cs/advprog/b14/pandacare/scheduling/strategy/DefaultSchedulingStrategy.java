package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSchedulingStrategy implements SchedulingStrategy {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultSchedulingStrategy.class);
    private final ScheduleRepository scheduleRepository;
    private final ConsultationRepository consultationRepository;
    
    public DefaultSchedulingStrategy(ScheduleRepository scheduleRepository, ConsultationRepository consultationRepository) {
        this.scheduleRepository = scheduleRepository;
        this.consultationRepository = consultationRepository;
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
        
        // First check if the schedule is available
        if (!scheduleRepository.isScheduleAvailable(caregiverId, schedule)) {
            log.warn("Schedule not available: {}", schedule);
            return false;
        }
        
        // Book the consultation with status BOOKED
        return consultationRepository.saveConsultation(caregiverId, pacilianId, schedule, "BOOKED");
    }
    
    @Override
    public boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status) {
        log.info("Updating consultation status for caregiver {}, patient {}, schedule {}: {}",
                caregiverId, pacilianId, schedule, status);
        
        return consultationRepository.updateStatus(caregiverId, pacilianId, schedule, status);
    }
}