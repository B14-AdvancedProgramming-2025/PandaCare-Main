package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SchedulingStrategy {
    List<Consultation> getCaregiverConsultations(String caregiverId);
    
    List<Consultation> getPacilianConsultations(String pacilianId);

    List<String> getCaregiverSchedules(String caregiverId);
    
    boolean createScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    boolean bookConsultationWithDateTime(String caregiverId, String pacilianId, 
                                    LocalDateTime startTime, LocalDateTime endTime);
    
    boolean updateConsultationStatusWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime, String status);
    
    boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    
    boolean modifyScheduleWithDateTime(String caregiverId, 
                                    LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                    LocalDateTime newStartTime, LocalDateTime newEndTime);

    List<Map<String, Object>> findAvailableCaregivers(
            LocalDateTime startTime, LocalDateTime endTime, String specialty);

    List<Map<String, Object>> getCaregiverSchedulesFormatted(String caregiverId);
}