package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ConsultationTest {
    
    @Test
    public void testConsultationWithDateTime() {
        String id = "CONS1";
        String caregiverId = "C001";
        String pacilianId = "P001";
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        String status = "BOOKED";
        
        Consultation consultation = new Consultation(id, caregiverId, pacilianId, startTime, endTime, status);
        
        assertEquals(id, consultation.getId());
        assertEquals(caregiverId, consultation.getCaregiverId());
        assertEquals(pacilianId, consultation.getPacilianId());
        assertEquals(startTime, consultation.getStartTime());
        assertEquals(endTime, consultation.getEndTime());
        assertEquals(status, consultation.getStatus());
    }
    
    @Test
    public void testConsultationDateTimeSetters() {
        Consultation consultation = new Consultation();
        
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        consultation.setStartTime(startTime);
        consultation.setEndTime(endTime);
        
        assertEquals(startTime, consultation.getStartTime());
        assertEquals(endTime, consultation.getEndTime());
    }
}