package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import java.util.List;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;

public interface ConsultationRepository {
    boolean saveConsultation(String caregiverId, String pacilianId, String schedule, String status);
    boolean updateStatus(String caregiverId, String pacilianId, String schedule, String status);
    List<Consultation> findConsultationsByCaregiverId(String caregiverId);
    List<Consultation> findConsultationsByPacilianId(String pacilianId);
}