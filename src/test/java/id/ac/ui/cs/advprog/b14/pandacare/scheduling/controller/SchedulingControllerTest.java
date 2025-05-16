package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SchedulingControllerTest {

    @Mock
    private SchedulingService schedulingService;
    
    @InjectMocks
    private SchedulingController controller;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreateSchedule() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("schedule", "Monday 10:00-12:00");
        
        when(schedulingService.createSchedule("C001", "Monday 10:00-12:00")).thenReturn(true);
        
        ResponseEntity<Map<String, Object>> response = controller.createSchedule(requestBody);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule created successfully", response.getBody().get("message"));
        verify(schedulingService).createSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testCreateScheduleFailed() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("schedule", "Monday 10:00-12:00");
        
        when(schedulingService.createSchedule("C001", "Monday 10:00-12:00")).thenReturn(false);
        
        ResponseEntity<Map<String, Object>> response = controller.createSchedule(requestBody);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to create schedule", response.getBody().get("message"));
        verify(schedulingService).createSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testBookConsultation() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("pacilianId", "P001");
        requestBody.put("schedule", "Monday 10:00-12:00");
        
        when(schedulingService.bookConsultation("C001", "P001", "Monday 10:00-12:00")).thenReturn(true);
        
        ResponseEntity<Map<String, Object>> response = controller.bookConsultation(requestBody);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation booked successfully", response.getBody().get("message"));
        verify(schedulingService).bookConsultation("C001", "P001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testAcceptConsultation() {
        String caregiverId = "C001";
        String pacilianId = "P001";
        String schedule = "Monday 10:00-12:00";
        
        when(schedulingService.acceptConsultation(caregiverId, pacilianId, schedule)).thenReturn(true);
        
        ResponseEntity<Map<String, Object>> response = controller.acceptConsultation(caregiverId, pacilianId, schedule);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation accepted successfully", response.getBody().get("message"));
        verify(schedulingService).acceptConsultation(caregiverId, pacilianId, schedule);
    }

    @Test
    public void testRejectConsultation() {
        String caregiverId = "C001";
        String pacilianId = "P001";
        String schedule = "Monday 10:00-12:00";
        
        when(schedulingService.rejectConsultation(caregiverId, pacilianId, schedule)).thenReturn(true);
        
        ResponseEntity<Map<String, Object>> response = controller.rejectConsultation(caregiverId, pacilianId, schedule);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation rejected successfully", response.getBody().get("message"));
        verify(schedulingService).rejectConsultation(caregiverId, pacilianId, schedule);
    }

    @Test
    public void testModifyConsultation() {
        String caregiverId = "C001";
        String pacilianId = "P001";
        String schedule = "Monday 10:00-12:00";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newSchedule", "Tuesday 14:00-16:00");
        
        when(schedulingService.modifyConsultation(caregiverId, pacilianId, schedule)).thenReturn(true);
        
        ResponseEntity<Map<String, Object>> response = controller.modifyConsultation(caregiverId, pacilianId, schedule, requestBody);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation modified successfully", response.getBody().get("message"));
        verify(schedulingService).modifyConsultation(caregiverId, pacilianId, schedule);
    }
}