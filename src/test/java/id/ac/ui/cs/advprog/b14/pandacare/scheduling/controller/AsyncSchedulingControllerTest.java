package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.AsyncSchedulingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

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
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AsyncSchedulingController controller;

    private DateTimeFormatter formatter;
    private User caregiverUser;
    private User pacilianUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        // Create mock users
        caregiverUser = org.mockito.Mockito.mock(Caregiver.class);
        when(caregiverUser.getId()).thenReturn("C001");
        when(caregiverUser.getType()).thenReturn(UserType.CAREGIVER);
        
        pacilianUser = org.mockito.Mockito.mock(Pacilian.class);
        when(pacilianUser.getId()).thenReturn("P001");
        when(pacilianUser.getType()).thenReturn(UserType.PACILIAN);
    }

    @Test
    public void testGetCaregiverConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        List<Consultation> consultations = new ArrayList<>();
        
        when(asyncSchedulingService.getCaregiverConsultationsAsync(caregiverId))
                .thenReturn(CompletableFuture.completedFuture(consultations));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(asyncSchedulingService).getCaregiverConsultationsAsync(caregiverId);
    }

    @Test
    public void testGetCaregiverConsultationsAsyncUnauthorized() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can access caregiver consultations", response.getBody().get("message"));
    }

    @Test
    public void testGetCaregiverConsultationsAsyncWrongCaregiver() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C002";  // Different from authenticated user
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("You can only view your own consultations", response.getBody().get("message"));
    }

    @Test
    public void testGetPatientConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String pacilianId = "P001";
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        List<Consultation> consultations = new ArrayList<>();
        
        when(asyncSchedulingService.getPatientConsultationsAsync(pacilianId))
                .thenReturn(CompletableFuture.completedFuture(consultations));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getPatientConsultationsAsync(pacilianId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(asyncSchedulingService).getPatientConsultationsAsync(pacilianId);
    }

    @Test
    public void testGetPatientConsultationsAsyncUnauthorized() throws ExecutionException, InterruptedException {
        // Setup
        String pacilianId = "P001";
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getPatientConsultationsAsync(pacilianId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only patients can access patient consultations", response.getBody().get("message"));
    }

    @Test
    public void testCreateScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(asyncSchedulingService.createScheduleWithDateTimeAsync(eq("C001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule created successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).createScheduleWithDateTimeAsync("C001", startTime, endTime);
    }

    @Test
    public void testCreateScheduleAsyncUnauthorized() throws ExecutionException, InterruptedException {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can create schedules", response.getBody().get("message"));
    }

    @Test
    public void testCreateScheduleAsyncFailed() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(asyncSchedulingService.createScheduleWithDateTimeAsync(eq("C001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(false));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to create schedule", response.getBody().get("message"));
        verify(asyncSchedulingService).createScheduleWithDateTimeAsync("C001", startTime, endTime);
    }
    
    @Test
    public void testBookConsultationAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(asyncSchedulingService.bookConsultationWithDateTimeAsync(eq("C001"), eq("P001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.bookConsultationAsync(requestBody, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation booked successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).bookConsultationWithDateTimeAsync("C001", "P001", startTime, endTime);
    }

    @Test
    public void testAcceptConsultationAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15 10:00";
        String endTimeStr = "2025-06-15 12:00";
        
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
        
        when(asyncSchedulingService.acceptConsultationWithDateTimeAsync(eq(caregiverId), eq(pacilianId), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.acceptConsultationAsync(caregiverId, pacilianId, startTimeStr, endTimeStr, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation accepted successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).acceptConsultationWithDateTimeAsync(caregiverId, pacilianId, startTime, endTime);
    }

    @Test
    public void testRejectConsultationAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15 10:00";
        String endTimeStr = "2025-06-15 12:00";
        
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
        
        when(asyncSchedulingService.rejectConsultationWithDateTimeAsync(eq(caregiverId), eq(pacilianId), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.rejectConsultationAsync(caregiverId, pacilianId, startTimeStr, endTimeStr, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation rejected successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).rejectConsultationWithDateTimeAsync(caregiverId, pacilianId, startTime, endTime);
    }

    @Test
    public void testModifyScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("oldStartTime", "2025-06-15 10:00");
        requestBody.put("oldEndTime", "2025-06-15 12:00");
        requestBody.put("newStartTime", "2025-06-16 14:00");
        requestBody.put("newEndTime", "2025-06-16 16:00");

        LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
        LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
        LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);

        when(asyncSchedulingService.modifyScheduleWithDateTimeAsync(
                eq("C001"), eq(oldStartTime), eq(oldEndTime), eq(newStartTime), eq(newEndTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.modifyScheduleAsync(requestBody, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule modified successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).modifyScheduleWithDateTimeAsync(
                "C001", oldStartTime, oldEndTime, newStartTime, newEndTime);
    }

    @Test
    public void testDeleteScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        String startTimeStr = "2025-06-15 10:00";
        String endTimeStr = "2025-06-15 12:00";
        
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
        
        when(asyncSchedulingService.deleteScheduleWithDateTimeAsync(eq(caregiverId), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.deleteScheduleAsync(caregiverId, startTimeStr, endTimeStr, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule deleted successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).deleteScheduleWithDateTimeAsync(caregiverId, startTime, endTime);
    }

    @Test
    public void testGetCaregiverSchedulesAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        List<Map<String, Object>> schedules = new ArrayList<>();
        
        when(asyncSchedulingService.getCaregiverSchedulesFormattedAsync(caregiverId))
                .thenReturn(CompletableFuture.completedFuture(schedules));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverSchedulesAsync(caregiverId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(schedules, response.getBody().get("schedules"));
        verify(asyncSchedulingService).getCaregiverSchedulesFormattedAsync(caregiverId);
    }

    @Test
    public void testFindAvailableCaregiversAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
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
                controller.findAvailableCaregiversAsync(startTimeStr, endTimeStr, specialty, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(caregivers, response.getBody().get("caregivers"));
        verify(asyncSchedulingService).findAvailableCaregiversAsync(startTime, endTime, specialty);
    }

    @Test
    public void testErrorHandlingInFindAvailableCaregiversAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        String startTimeStr = "invalid-date";
        String endTimeStr = "2025-06-15 12:00";
        String specialty = "Cardiology";

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.findAvailableCaregiversAsync(startTimeStr, endTimeStr, specialty, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Invalid request format"));
}

    @Test
    public void testExceptionalHandlingInGetCaregiverConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        CompletableFuture<List<Consultation>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Service unavailable"));
        
        when(asyncSchedulingService.getCaregiverConsultationsAsync(caregiverId))
                .thenReturn(failedFuture);

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverId, authentication);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Failed to get consultations"));
    }
}