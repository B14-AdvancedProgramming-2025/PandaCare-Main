package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulingStrategy {
    
    // Legacy methods for backward compatibility
    boolean createSchedule(String caregiverId, String schedule);
    
    boolean bookConsultation(String caregiverId, String pacilianId, String schedule);
    
    boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status);
    
    boolean deleteSchedule(String caregiverId, String schedule);
    
    boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule);
    
    List<Consultation> getCaregiverConsultations(String caregiverId);
    
    List<Consultation> getPatientConsultations(String pacilianId);
    
    // New methods with DateTime
    boolean createScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    boolean bookConsultationWithDateTime(String caregiverId, String pacilianId, 
                                    LocalDateTime startTime, LocalDateTime endTime);
    
    boolean updateConsultationStatusWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime, String status);
    
    boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    boolean modifyScheduleWithDateTime(String caregiverId, 
                                    LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                    LocalDateTime newStartTime, LocalDateTime newEndTime);
}