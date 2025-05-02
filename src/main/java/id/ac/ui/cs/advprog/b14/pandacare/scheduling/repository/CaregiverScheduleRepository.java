package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CaregiverScheduleRepository implements ScheduleRepository {

    private static final Logger log = LoggerFactory.getLogger(CaregiverScheduleRepository.class);
    private final CaregiverRepositoryAdapter caregiverAdapter;
    
    public CaregiverScheduleRepository(CaregiverRepositoryAdapter caregiverAdapter) {
        this.caregiverAdapter = caregiverAdapter;
    }
    
    @Override
    public boolean saveSchedule(String caregiverId, String schedule) {
        try {
            Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
            
            if (!optionalCaregiver.isPresent()) {
                log.error("Caregiver not found: {}", caregiverId);
                return false;
            }
            
            Caregiver caregiver = optionalCaregiver.get();
            caregiver.getWorkingSchedule().add(schedule);
            caregiverAdapter.save(caregiver);
            return true;
        } catch (Exception e) {
            log.error("Error saving schedule", e);
            return false;
        }
    }
    
    @Override
    public boolean isScheduleAvailable(String caregiverId, String schedule) {
        try {
            Optional<Caregiver> optionalCaregiver = caregiverAdapter.findById(caregiverId);
            
            if (!optionalCaregiver.isPresent()) {
                log.error("Caregiver not found: {}", caregiverId);
                return false;
            }
            
            Caregiver caregiver = optionalCaregiver.get();
            return caregiver.getWorkingSchedule().contains(schedule);
        } catch (Exception e) {
            log.error("Error checking schedule availability", e);
            return false;
        }
    }
}