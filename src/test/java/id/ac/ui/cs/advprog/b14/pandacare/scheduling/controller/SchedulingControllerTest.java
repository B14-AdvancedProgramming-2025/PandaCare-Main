package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SchedulingControllerTest {

    @Mock
    private SchedulingService asyncSchedulingService;

    @InjectMocks
    private SchedulingController controller;

    private DateTimeFormatter formatter;
    private User caregiverUser;
    private User pacilianUser;

    @BeforeEach
    public void setup() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        // Create mock users
        caregiverUser = mock(Caregiver.class);
        when(caregiverUser.getId()).thenReturn("C001");
        when(caregiverUser.getEmail()).thenReturn("caregiver@example.com");
        when(caregiverUser.getType()).thenReturn(UserType.CAREGIVER);
        
        pacilianUser = mock(Pacilian.class);
        when(pacilianUser.getId()).thenReturn("P001");
        when(pacilianUser.getEmail()).thenReturn("pacilian@example.com");
        when(pacilianUser.getType()).thenReturn(UserType.PACILIAN);
    }

    @Test
    public void testGetCaregiverConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        List<Consultation> consultations = new ArrayList<>();
        
        when(asyncSchedulingService.getCaregiverConsultationsAsync("C001"))
                .thenReturn(CompletableFuture.completedFuture(consultations));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(asyncSchedulingService).getCaregiverConsultationsAsync("C001");
    }

    @Test
    public void testGetCaregiverConsultationsAsyncWithNullUser() throws ExecutionException, InterruptedException {
        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(null);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("User not authenticated or not found.", response.getBody().get("message"));
    }

    @Test
    public void testGetCaregiverConsultationsAsyncWithWrongUserType() throws ExecutionException, InterruptedException {
        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can access caregiver consultations", response.getBody().get("message"));
    }

    @Test
    public void testGetPacilianConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        List<Consultation> consultations = new ArrayList<>();
        
        when(asyncSchedulingService.getPacilianConsultationsAsync("P001"))
                .thenReturn(CompletableFuture.completedFuture(consultations));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getPacilianConsultationsAsync(pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(asyncSchedulingService).getPacilianConsultationsAsync("P001");
    }

    @Test
    public void testGetPacilianConsultationsAsyncWithWrongUserType() throws ExecutionException, InterruptedException {
        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getPacilianConsultationsAsync(caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only pacilians can access pacilian consultations", response.getBody().get("message"));
    }

    @Test
    public void testCreateScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(asyncSchedulingService.createScheduleWithDateTimeAsync(eq("C001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule created successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).createScheduleWithDateTimeAsync("C001", startTime, endTime);
    }

    @Test
    public void testCreateScheduleAsyncWithWrongUserType() throws ExecutionException, InterruptedException {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can create schedules", response.getBody().get("message"));
    }

    @Test
    public void testBookConsultationAsync() throws ExecutionException, InterruptedException {
        // Setup
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
                controller.bookConsultationAsync(requestBody, pacilianUser);
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
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("pacilianId", "P001");
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");
        
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
        when(asyncSchedulingService.acceptConsultationWithDateTimeAsync(eq("C001"), eq("P001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.acceptConsultationAsync(requestBody, caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation accepted successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).acceptConsultationWithDateTimeAsync("C001", "P001", startTime, endTime);
    }

    @Test
    public void testAcceptConsultationAsyncWithWrongUserType() throws ExecutionException, InterruptedException {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("pacilianId", "P001");
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        // Execute - attempt to accept as pacilian (wrong user type)
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.acceptConsultationAsync(requestBody, pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can accept consultations", response.getBody().get("message"));
    }

    @Test
    public void testRejectConsultationAsync() throws ExecutionException, InterruptedException {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("pacilianId", "P001");
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");
        
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
        when(asyncSchedulingService.rejectConsultationWithDateTimeAsync(eq("C001"), eq("P001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.rejectConsultationAsync(requestBody, caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation rejected successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).rejectConsultationWithDateTimeAsync("C001", "P001", startTime, endTime);
    }

    @Test
    public void testModifyScheduleAsync() throws ExecutionException, InterruptedException {
        // Setup
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
                controller.modifyScheduleAsync(requestBody, caregiverUser);
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
        // Setup - Now uses @RequestBody instead of path parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");
        
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
        when(asyncSchedulingService.deleteScheduleWithDateTimeAsync(eq("C001"), eq(startTime), eq(endTime)))
                .thenReturn(CompletableFuture.completedFuture(true));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.deleteScheduleAsync(requestBody, caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule deleted successfully", response.getBody().get("message"));
        verify(asyncSchedulingService).deleteScheduleWithDateTimeAsync("C001", startTime, endTime);
    }

    @Test
    public void testDeleteScheduleAsyncWithWrongUserType() throws ExecutionException, InterruptedException {
        // Setup
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        // Execute - attempt to delete as pacilian (wrong user type)
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.deleteScheduleAsync(requestBody, pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can delete schedules", response.getBody().get("message"));
    }

    @Test
    public void testGetCaregiverSchedulesAsync() throws ExecutionException, InterruptedException {
        // Setup
        List<Map<String, Object>> schedules = new ArrayList<>();
        
        when(asyncSchedulingService.getCaregiverSchedulesFormattedAsync("C001"))
                .thenReturn(CompletableFuture.completedFuture(schedules));

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverSchedulesAsync(caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(schedules, response.getBody().get("schedules"));
        verify(asyncSchedulingService).getCaregiverSchedulesFormattedAsync("C001");
    }

    @Test
    public void testFindAvailableCaregiversAsync() throws ExecutionException, InterruptedException {
        // Setup
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
                controller.findAvailableCaregiversAsync(startTimeStr, endTimeStr, specialty, pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(caregivers, response.getBody().get("caregivers"));
        verify(asyncSchedulingService).findAvailableCaregiversAsync(startTime, endTime, specialty);
    }

    @Test
    public void testFindAvailableCaregiversAsyncWithWrongUserType() throws ExecutionException, InterruptedException {
        // Setup
        String startTimeStr = "2025-06-15 10:00";
        String endTimeStr = "2025-06-15 12:00";
        String specialty = "Cardiology";

        // Execute - attempt to find caregivers as a caregiver (not a pacilian)
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.findAvailableCaregiversAsync(startTimeStr, endTimeStr, specialty, caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only pacilians can find available caregivers", response.getBody().get("message"));
    }

    @Test
    public void testErrorHandlingInFindAvailableCaregiversAsync() throws ExecutionException, InterruptedException {
        // Setup - invalid date format
        String startTimeStr = "invalid-date";
        String endTimeStr = "2025-06-15 12:00";
        String specialty = "Cardiology";

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.findAvailableCaregiversAsync(startTimeStr, endTimeStr, specialty, pacilianUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Invalid request format"));
    }

    @Test
    public void testServiceExceptionHandling() throws ExecutionException, InterruptedException {
        // Setup - service throws exception
        CompletableFuture<List<Consultation>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Service unavailable"));
        
        when(asyncSchedulingService.getCaregiverConsultationsAsync("C001"))
                .thenReturn(failedFuture);

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.getCaregiverConsultationsAsync(caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Failed to get consultations"));
    }

    @Test
    public void testNullUserHandlingForAllEndpoints() throws ExecutionException, InterruptedException {
        // Test create schedule with null user
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00");
        requestBody.put("endTime", "2025-06-15 12:00");

        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, null);
        ResponseEntity<Map<String, Object>> response = future.get();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("User not authenticated or not found.", response.getBody().get("message"));
    }

    @Test
    public void testInvalidDateFormatHandling() throws ExecutionException, InterruptedException {
        // Setup - invalid date format in request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "invalid-date");
        requestBody.put("endTime", "2025-06-15 12:00");

        // Execute
        CompletableFuture<ResponseEntity<Map<String, Object>>> future = 
                controller.createScheduleAsync(requestBody, caregiverUser);
        ResponseEntity<Map<String, Object>> response = future.get();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(((String)response.getBody().get("message")).contains("Invalid request format"));
    }
}