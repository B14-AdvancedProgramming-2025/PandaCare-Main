package id.ac.ui.cs.advprog.b14.pandacare.scheduling;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulingContext {
    
    private static final Logger log = LoggerFactory.getLogger(SchedulingContext.class);
    private SchedulingStrategy strategy;
    
    public void setStrategy(SchedulingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<String> getCaregiverSchedules(String caregiverId) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return new ArrayList<>();
        }
        return strategy.getCaregiverSchedules(caregiverId);
    }

    public List<Consultation> getCaregiverConsultations(String caregiverId) {
        return strategy.getCaregiverConsultations(caregiverId);
    }

    public List<Consultation> getPatientConsultations(String pacilianId) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return new ArrayList<>();
        }
        return strategy.getPatientConsultations(pacilianId);
    }

    public boolean createScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.createScheduleWithDateTime(caregiverId, startTime, endTime);
    }
    
    public boolean bookConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.bookConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
    }
    
    public boolean updateConsultationStatusWithDateTime(String caregiverId, String pacilianId, 
                                                    LocalDateTime startTime, LocalDateTime endTime, String status) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.updateConsultationStatusWithDateTime(caregiverId, pacilianId, startTime, endTime, status);
    }
    
    public boolean deleteScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime startTime, LocalDateTime endTime) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.deleteScheduleWithDateTime(caregiverId, startTime, endTime);
    }
    
    public boolean modifyScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                        LocalDateTime newStartTime, LocalDateTime newEndTime) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.modifyScheduleWithDateTime(
            caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
    }

    public List<Map<String, Object>> findAvailableCaregivers(
            LocalDateTime startTime, LocalDateTime endTime, String specialty) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return new ArrayList<>();
        }
        return strategy.findAvailableCaregivers(startTime, endTime, specialty);
    }

    public List<Map<String, Object>> getCaregiverSchedulesFormatted(String caregiverId) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return new ArrayList<>();
        }
        return strategy.getCaregiverSchedulesFormatted(caregiverId);
    }
}