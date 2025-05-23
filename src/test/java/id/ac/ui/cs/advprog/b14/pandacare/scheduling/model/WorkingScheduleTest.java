package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkingScheduleTest {
    
    @Test
    public void testWorkingScheduleCreation() {
        Long id = 1L;
        String caregiverId = "C001";
        String schedule = "Monday 10:00-12:00";
        String status = "AVAILABLE";
        boolean available = true;
        
        WorkingSchedule workingSchedule = new WorkingSchedule(id, caregiverId, schedule, status, available);
        
        assertEquals(id, workingSchedule.getId());
        assertEquals(caregiverId, workingSchedule.getCaregiverId());
        assertEquals(schedule, workingSchedule.getSchedule());
        assertEquals(status, workingSchedule.getStatus());
        assertTrue(workingSchedule.isAvailable());
    }
    
    @Test
    public void testWorkingScheduleNoArgsConstructor() {
        WorkingSchedule workingSchedule = new WorkingSchedule();
        assertNotNull(workingSchedule);
    }
    
    @Test
    public void testSetters() {
        WorkingSchedule workingSchedule = new WorkingSchedule();
        
        workingSchedule.setId(1L);
        workingSchedule.setCaregiverId("C001");
        workingSchedule.setSchedule("Monday 10:00-12:00");
        workingSchedule.setStatus("AVAILABLE");
        workingSchedule.setAvailable(true);
        
        assertEquals(1L, workingSchedule.getId());
        assertEquals("C001", workingSchedule.getCaregiverId());
        assertEquals("Monday 10:00-12:00", workingSchedule.getSchedule());
        assertEquals("AVAILABLE", workingSchedule.getStatus());
        assertTrue(workingSchedule.isAvailable());
    }
    
    @Test
    public void testUpdateAvailability() {
        WorkingSchedule workingSchedule = new WorkingSchedule(1L, "C001", "Monday 10:00-12:00", "AVAILABLE", true);
        
        workingSchedule.setAvailable(false);
        workingSchedule.setStatus("BOOKED");
        
        assertFalse(workingSchedule.isAvailable());
        assertEquals("BOOKED", workingSchedule.getStatus());
    }
}