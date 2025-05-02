package id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import java.util.Optional;

/**
 * Adapter interface for accessing Caregiver entities
 */
public interface CaregiverRepositoryAdapter {
    
    /**
     * Find caregiver by ID
     * 
     * @param id the caregiver ID
     * @return Optional containing the caregiver if found
     */
    Optional<Caregiver> findById(String id);
    
    /**
     * Save caregiver data
     * 
     * @param caregiver the caregiver to save
     * @return the saved caregiver
     */
    Caregiver save(Caregiver caregiver);
}