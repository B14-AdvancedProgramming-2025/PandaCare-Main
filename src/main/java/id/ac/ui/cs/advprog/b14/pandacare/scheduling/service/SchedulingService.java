package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SchedulingService {
    
    private final SchedulingContext context;
    
    public SchedulingService(SchedulingContext context, DefaultSchedulingStrategy defaultStrategy) {
        this.context = context;
        this.context.setStrategy(defaultStrategy);
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

    public boolean createScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        return context.createScheduleWithDateTime(caregiverId, startTime, endTime);
    }
    
    public boolean bookConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime) {
        return context.bookConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
    }
    
    public boolean acceptConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime) {
        return context.updateConsultationStatusWithDateTime(
            caregiverId, pacilianId, startTime, endTime, "ACCEPTED");
    }
    
    public boolean rejectConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime) {
        return context.updateConsultationStatusWithDateTime(
            caregiverId, pacilianId, startTime, endTime, "REJECTED");
    }
    
    public boolean modifyConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime) {
        return context.updateConsultationStatusWithDateTime(
            caregiverId, pacilianId, startTime, endTime, "MODIFIED");
    }
    
    public boolean modifyScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                        LocalDateTime newStartTime, LocalDateTime newEndTime) {
        return context.modifyScheduleWithDateTime(
            caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
    }
    
    public boolean deleteScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime startTime, LocalDateTime endTime) {
        return context.deleteScheduleWithDateTime(caregiverId, startTime, endTime);
    }

    public List<Map<String, Object>> findAvailableCaregivers(
            LocalDateTime startTime, LocalDateTime endTime, String specialty) {
        return context.findAvailableCaregivers(startTime, endTime, specialty);
    }

    public List<Map<String, Object>> getCaregiverSchedulesFormatted(String caregiverId) {
        return context.getCaregiverSchedulesFormatted(caregiverId);
    }
}