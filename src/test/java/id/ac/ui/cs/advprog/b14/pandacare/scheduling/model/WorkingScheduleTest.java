package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WorkingScheduleTest {
    
    @Test
    public void testWorkingScheduleWithDateTime() {
        String id = UUID.randomUUID().toString();
        String caregiverId = "C001";
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        String status = "AVAILABLE";
        boolean available = true;
        
        WorkingSchedule workingSchedule = new WorkingSchedule(id, caregiverId, startTime, endTime, status, available);
        
        assertEquals(id, workingSchedule.getId());
        assertEquals(caregiverId, workingSchedule.getCaregiverId());
        assertEquals(startTime, workingSchedule.getStartTime());
        assertEquals(endTime, workingSchedule.getEndTime());
        assertEquals(status, workingSchedule.getStatus());
        assertTrue(workingSchedule.isAvailable());
    }
    
    @Test
    public void testWorkingScheduleSetters() {
        WorkingSchedule workingSchedule = new WorkingSchedule();
        
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        workingSchedule.setStartTime(startTime);
        workingSchedule.setEndTime(endTime);
        
        assertEquals(startTime, workingSchedule.getStartTime());
        assertEquals(endTime, workingSchedule.getEndTime());
    }
}