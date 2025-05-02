package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

public interface ConsultationRepository {
    boolean saveConsultation(String caregiverId, String pacilianId, String schedule, String status);
    boolean updateStatus(String caregiverId, String pacilianId, String schedule, String status);
}