package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchedulingControllerTest {

    @Mock
    private SchedulingService schedulingService;

    @InjectMocks
    private SchedulingController controller;

    private DateTimeFormatter formatter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    }

    @Test
    public void testCreateScheduleWithDateTime() {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("startTime", "2025-06-15T10:00");
        requestBody.put("endTime", "2025-06-15T12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.createScheduleWithDateTime("C001", startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.createScheduleWithDateTime(requestBody);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule created successfully", response.getBody().get("message"));
        verify(schedulingService).createScheduleWithDateTime("C001", startTime, endTime);
    }

    @Test
    public void testCreateScheduleWithDateTimeFailed() {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("startTime", "2025-06-15T10:00");
        requestBody.put("endTime", "2025-06-15T12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.createScheduleWithDateTime("C001", startTime, endTime)).thenReturn(false);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.createScheduleWithDateTime(requestBody);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to create schedule", response.getBody().get("message"));
        verify(schedulingService).createScheduleWithDateTime("C001", startTime, endTime);
    }

    @Test
    public void testBookConsultationWithDateTime() {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("pacilianId", "P001");
        requestBody.put("startTime", "2025-06-15T10:00");
        requestBody.put("endTime", "2025-06-15T12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.bookConsultationWithDateTime("C001", "P001", startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.bookConsultationWithDateTime(requestBody);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation booked successfully", response.getBody().get("message"));
        verify(schedulingService).bookConsultationWithDateTime("C001", "P001", startTime, endTime);
    }

    @Test
    public void testAcceptConsultationWithDateTime() {
        // Setup
        String caregiverId = "C001";
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15T10:00";
        String endTimeStr = "2025-06-15T12:00";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        when(schedulingService.acceptConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.acceptConsultationWithDateTime(
                caregiverId, pacilianId, startTimeStr, endTimeStr);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation accepted successfully", response.getBody().get("message"));
        verify(schedulingService).acceptConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
    }

    @Test
    public void testRejectConsultationWithDateTime() {
        // Setup
        String caregiverId = "C001";
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15T10:00";
        String endTimeStr = "2025-06-15T12:00";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        when(schedulingService.rejectConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.rejectConsultationWithDateTime(
                caregiverId, pacilianId, startTimeStr, endTimeStr);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation rejected successfully", response.getBody().get("message"));
        verify(schedulingService).rejectConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
    }

    @Test
    public void testModifyScheduleWithDateTime() {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("oldStartTime", "2025-06-15T10:00");
        requestBody.put("oldEndTime", "2025-06-15T12:00");
        requestBody.put("newStartTime", "2025-06-16T14:00");
        requestBody.put("newEndTime", "2025-06-16T16:00");

        LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
        LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
        LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);

        when(schedulingService.modifyScheduleWithDateTime(
                "C001", oldStartTime, oldEndTime, newStartTime, newEndTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.modifyScheduleWithDateTime(requestBody);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule modified successfully", response.getBody().get("message"));
        verify(schedulingService).modifyScheduleWithDateTime(
                "C001", oldStartTime, oldEndTime, newStartTime, newEndTime);
    }

    @Test
    public void testDeleteScheduleWithDateTime() {
        // Setup
        String caregiverId = "C001";
        String startTimeStr = "2025-06-15T10:00";
        String endTimeStr = "2025-06-15T12:00";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        when(schedulingService.deleteScheduleWithDateTime(caregiverId, startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.deleteScheduleWithDateTime(
                caregiverId, startTimeStr, endTimeStr);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule deleted successfully", response.getBody().get("message"));
        verify(schedulingService).deleteScheduleWithDateTime(caregiverId, startTime, endTime);
    }
}