package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConsultationTest {
    
    @Test
    public void testConsultationCreation() {
        String id = "CONS1";
        String caregiverId = "C001";
        String pacilianId = "P001";
        String scheduleTime = "Monday 10:00-12:00";
        String status = "BOOKED";
        
        Consultation consultation = new Consultation(id, caregiverId, pacilianId, scheduleTime, status);
        
        assertEquals(id, consultation.getId());
        assertEquals(caregiverId, consultation.getCaregiverId());
        assertEquals(pacilianId, consultation.getPacilianId());
        assertEquals(scheduleTime, consultation.getScheduleTime());
        assertEquals(status, consultation.getStatus());
    }
    
    @Test
    public void testSetStatus() {
        Consultation consultation = new Consultation("CONS1", "C001", "P001", "Monday 10:00-12:00", "BOOKED");
        
        consultation.setStatus("ACCEPTED");
        
        assertEquals("ACCEPTED", consultation.getStatus());
    }
}