package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

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
}