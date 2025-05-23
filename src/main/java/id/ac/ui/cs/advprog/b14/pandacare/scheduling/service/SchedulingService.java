package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SchedulingService {
    
    private final SchedulingContext context;
    
    public SchedulingService(SchedulingContext context, DefaultSchedulingStrategy defaultStrategy) {
        this.context = context;
        this.context.setStrategy(defaultStrategy);
    }
    
    public boolean createSchedule(String caregiverId, String schedule) {
        return context.createSchedule(caregiverId, schedule);
    }
    
    public boolean bookConsultation(String caregiverId, String pacilianId, String schedule) {
        return context.bookConsultation(caregiverId, pacilianId, schedule);
    }
    
    public boolean acceptConsultation(String caregiverId, String pacilianId, String schedule) {
        return context.updateConsultationStatus(caregiverId, pacilianId, schedule, "ACCEPTED");
    }
    
    public boolean rejectConsultation(String caregiverId, String pacilianId, String schedule) {
        return context.updateConsultationStatus(caregiverId, pacilianId, schedule, "REJECTED");
    }
    
    public boolean modifyConsultation(String caregiverId, String pacilianId, String schedule) {
        return context.updateConsultationStatus(caregiverId, pacilianId, schedule, "MODIFIED");
    }

    public List<String> getCaregiverSchedules(String caregiverId) {
        return context.getCaregiverSchedules(caregiverId);
    }

    public List<Consultation> getCaregiverConsultations(String caregiverId) {
        return context.getCaregiverConsultations(caregiverId);
    }

    public List<Consultation> getPatientConsultations(String pacilianId) {
        return context.getPatientConsultations(pacilianId);
    }

    public boolean deleteSchedule(String caregiverId, String schedule) {
        return context.deleteSchedule(caregiverId, schedule);
    }

    public boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule) {
        return context.modifySchedule(caregiverId, oldSchedule, newSchedule);
    }
}