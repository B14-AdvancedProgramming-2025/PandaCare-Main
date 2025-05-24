package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.AsyncSchedulingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncSchedulingControllerTest {

    @Mock
    private AsyncSchedulingService asyncSchedulingService;

    @InjectMocks
    private AsyncSchedulingController controller;

    private DateTimeFormatter formatter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Match the formatter used in the controller
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @Test
    public void testGetCaregiverConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        List<Consultation> consultations = new ArrayList<>();
        
        when(asyncSchedulingService.getCaregiverConsultationsAsync(caregiverId))
                .thenReturn(CompletableFuture.completedFuture(consultations));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverId);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(asyncSchedulingService).getCaregiverConsultationsAsync(caregiverId);
    }

    @Test
    public void testGetPatientConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String pacilianId = "P001";
        List<Consultation> consultations = new ArrayList<>();
        
        when(asyncSchedulingService.getPatientConsultationsAsync(pacilianId))
                .thenReturn(CompletableFuture.completedFuture(consultations));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getPatientConsultationsAsync(pacilianId);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(asyncSchedulingService).getPatientConsultationsAsync(pacilianId);
    }

    @Test
    public void testCreateScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        Map<String, String> requestBody = new HashMap<>();
        // Use the format matching the controller's formatter
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(asyncSchedulingService.createScheduleWithDateTimeAsync(eq(caregiverId), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(caregiverId, requestBody);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule created successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).createScheduleWithDateTimeAsync(caregiverId, startTime, endTime);
    }

    @Test
    public void testCreateScheduleAsyncFailed() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        Map<String, String> requestBody = new HashMap<>();
        // Use the format matching the controller's formatter
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(asyncSchedulingService.createScheduleWithDateTimeAsync(eq(caregiverId), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(false));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(caregiverId, requestBody);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to create schedule", response.getBody().get("message"));
        verify(asyncSchedulingService).createScheduleWithDateTimeAsync(caregiverId, startTime, endTime);
    }

    @Test
    public void testModifyScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        Map<String, String> requestBody = new HashMap<>();
        // Use the format matching the controller's formatter
        requestBody.put("oldStartTime", "2025-06-15 10:00");
        requestBody.put("oldEndTime", "2025-06-15 12:00");
        requestBody.put("newStartTime", "2025-06-16 14:00");
        requestBody.put("newEndTime", "2025-06-16 16:00");

        LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
        LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
        LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);

        when(asyncSchedulingService.modifyScheduleWithDateTimeAsync(
                eq(caregiverId), eq(oldStartTime), eq(oldEndTime), eq(newStartTime), eq(newEndTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.modifyScheduleAsync(caregiverId, requestBody);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule modified successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).modifyScheduleWithDateTimeAsync(
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
    }

    @Test
    public void testFindAvailableCaregiversAsync() throws ExecutionException, InterruptedException {
        // Setup
        // Use the format matching the controller's formatter
        String startTimeStr = "2025-06-15 10:00";
        String endTimeStr = "2025-06-15 12:00";
        String specialty = "Cardiology";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        List<Map<String, Object>> caregivers = new ArrayList<>();
        Map<String, Object> caregiver = new HashMap<>();
        caregiver.put("id", "C001");
        caregiver.put("name", "Dr. John Doe");
        caregivers.add(caregiver);

        when(asyncSchedulingService.findAvailableCaregiversAsync(eq(startTime), eq(endTime), eq(specialty)))
                .thenReturn(CompletableFuture.completedFuture(caregivers));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.findAvailableCaregiversAsync(startTimeStr, endTimeStr, specialty);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(caregivers, response.getBody().get("caregivers"));
        verify(asyncSchedulingService).findAvailableCaregiversAsync(startTime, endTime, specialty);
    }

    @Test
    public void testErrorHandlingInCreateScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        Map<String, String> requestBody = new HashMap<>();
        // Missing required fields
        
        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(caregiverId, requestBody);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Invalid request format"));
    }

    @Test
    public void testExceptionalHandlingInGetCaregiverConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        CompletableFuture<List<Consultation>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Service unavailable"));
        
        when(asyncSchedulingService.getCaregiverConsultationsAsync(caregiverId))
                .thenReturn(failedFuture);

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverId);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Failed to get consultations"));
    }
}