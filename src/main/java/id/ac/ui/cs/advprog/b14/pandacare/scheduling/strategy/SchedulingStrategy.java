package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import java.util.List;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;

public interface SchedulingStrategy {
    
    /**
     * Creates a schedule for a caregiver
     * 
     * @param caregiverId The ID of the caregiver
     * @param schedule The schedule time slot
     * @return true if successful, false otherwise
     */
    boolean createSchedule(String caregiverId, String schedule);
    
    /**
     * Books a consultation with a caregiver
     * 
     * @param caregiverId The ID of the caregiver
     * @param pacilianId The ID of the patient
     * @param schedule The schedule time slot
     * @return true if successful, false otherwise
     */
    boolean bookConsultation(String caregiverId, String pacilianId, String schedule);
    
    /**
     * Updates the status of a consultation
     * 
     * @param caregiverId The ID of the caregiver
     * @param pacilianId The ID of the patient
     * @param schedule The schedule time slot
     * @param status The new status (ACCEPTED, REJECTED, MODIFIED)
     * @return true if successful, false otherwise
     */
    boolean updateConsultationStatus(String caregiverId, String pacilianId, String schedule, String status);
    /**
     * Gets consultations for a caregiver
     * 
     * @param caregiverId The ID of the caregiver
     * @return List of consultations for the caregiver
     */
    List<Consultation> getCaregiverConsultations(String caregiverId);
    /**
     * Gets consultations for a patient
     * 
     * @param pacilianId The ID of the patient
     * @return List of consultations for the patient
     */
    List<Consultation> getPatientConsultations(String pacilianId);
    /**
     * Deletes a schedule for a caregiver
     * 
     * @param caregiverId The ID of the caregiver
     * @param schedule The schedule time slot to delete
     * @return true if successful, false otherwise
     */
    boolean deleteSchedule(String caregiverId, String schedule);

    /**
     * Modifies a schedule for a caregiver
     * 
     * @param caregiverId The ID of the caregiver
     * @param oldSchedule The old schedule time slot
     * @param newSchedule The new schedule time slot
     * @return true if successful, false otherwise
     */
    boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule);
}