package id.ac.ui.cs.advprog.b14.pandacare.scheduling;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;

public class SchedulingContext {
    
    private SchedulingStrategy strategy;
    
    public void setStrategy(SchedulingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public boolean createSchedule(String caregiverId, String schedule) {
        return false;
    }
    
    public boolean bookConsultation(String caregiverId, String pacilianId, String schedule) {
        return false;
    }
    
    public boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status) {
        return false;
    }
}