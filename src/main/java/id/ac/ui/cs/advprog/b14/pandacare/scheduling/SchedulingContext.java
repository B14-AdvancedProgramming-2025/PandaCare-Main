package id.ac.ui.cs.advprog.b14.pandacare.scheduling;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulingContext {
    
    private static final Logger log = LoggerFactory.getLogger(SchedulingContext.class);
    private SchedulingStrategy strategy;
    private final CaregiverRepositoryAdapter caregiverAdapter;

    public SchedulingContext(CaregiverRepositoryAdapter caregiverAdapter) {
        this.caregiverAdapter = caregiverAdapter;
    }
    
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

    public List<String> getCaregiverSchedules(String caregiverId) {
        Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
        if (optionalCaregiver.isPresent()) {
            return optionalCaregiver.get().getWorkingSchedule();
        }
        return new ArrayList<>();
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

    public boolean deleteSchedule(String caregiverId, String schedule) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.deleteSchedule(caregiverId, schedule);
    }

    public boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule) {
        if (strategy == null) {
            log.error("No scheduling strategy set");
            return false;
        }
        return strategy.modifySchedule(caregiverId, oldSchedule, newSchedule);
    }

    // New methods with DateTime
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
}