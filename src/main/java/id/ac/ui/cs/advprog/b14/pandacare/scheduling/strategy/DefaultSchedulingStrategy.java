package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;

public class DefaultSchedulingStrategy implements SchedulingStrategy {
    
    private final ScheduleRepository scheduleRepository;
    private final ConsultationRepository consultationRepository;
    
    public DefaultSchedulingStrategy(ScheduleRepository scheduleRepository, ConsultationRepository consultationRepository) {
        this.scheduleRepository = scheduleRepository;
        this.consultationRepository = consultationRepository;
    }
    
    @Override
    public boolean createSchedule(String caregiverId, String schedule) {
        return false;
    }
    
    @Override
    public boolean bookConsultation(String caregiverId, String pacilianId, String schedule) {
        return false;
    }
    
    @Override
    public boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status) {
        return false;
    }
}