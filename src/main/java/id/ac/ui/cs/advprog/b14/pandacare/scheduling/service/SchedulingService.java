package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {
    
    private final SchedulingContext context;
    
    public SchedulingService(SchedulingContext context, DefaultSchedulingStrategy defaultStrategy) {
        this.context = context;
        this.context.setStrategy(defaultStrategy);
    }
    
    public boolean createSchedule(String caregiverId, String schedule) {
        return false;
    }
    
    public boolean bookConsultation(String caregiverId, String pacilianId, String schedule) {
        return false;
    }
    
    public boolean acceptConsultation(String caregiverId, String pacilianId, String schedule) {
        return false;
    }
    
    public boolean rejectConsultation(String caregiverId, String pacilianId, String schedule) {
        return false;
    }
    
    public boolean modifyConsultation(String caregiverId, String pacilianId, String schedule) {
        return false;
    }
}