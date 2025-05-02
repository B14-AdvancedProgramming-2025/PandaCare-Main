package id.ac.ui.cs.advprog.b14.pandacare.scheduling;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulingContext {
    
    private static final Logger log = LoggerFactory.getLogger(SchedulingContext.class);
    private SchedulingStrategy strategy;
    
    public void setStrategy(SchedulingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public boolean createSchedule(String caregiverId, String schedule) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.createSchedule(caregiverId, schedule);
    }
    
    public boolean bookConsultation(String caregiverId, String pacilianId, String schedule) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.bookConsultation(caregiverId, pacilianId, schedule);
    }
    
    public boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.updateConsultationStatus(caregiverId, pacilianId, schedule, status);
    }
}