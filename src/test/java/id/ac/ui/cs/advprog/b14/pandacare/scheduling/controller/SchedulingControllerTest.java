package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchedulingControllerTest {

    @Mock
    private SchedulingService schedulingService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private Authentication authentication;

    @InjectMocks
    private SchedulingController controller;

    private DateTimeFormatter formatter;
    private Caregiver caregiverUser;
    private Pacilian pacilianUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Create mock users
        caregiverUser = org.mockito.Mockito.mock(Caregiver.class);
        when(caregiverUser.getId()).thenReturn("C001");
        when(caregiverUser.getType()).thenReturn(UserType.CAREGIVER);
        
        pacilianUser = org.mockito.Mockito.mock(Pacilian.class);
        when(pacilianUser.getId()).thenReturn("P001");
        when(pacilianUser.getType()).thenReturn(UserType.PACILIAN);
    }

    @Test
    public void testCreateScheduleWithDateTime() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00:00");
        requestBody.put("endTime", "2025-06-15 12:00:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.createScheduleWithDateTime("C001", startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.createSchedule(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule created successfully", response.getBody().get("message"));
        verify(schedulingService).createScheduleWithDateTime("C001", startTime, endTime);
    }

    @Test
    public void testCreateScheduleWithDateTimeFailed() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00:00");
        requestBody.put("endTime", "2025-06-15 12:00:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.createScheduleWithDateTime("C001", startTime, endTime)).thenReturn(false);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.createSchedule(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to create schedule", response.getBody().get("message"));
        verify(schedulingService).createScheduleWithDateTime("C001", startTime, endTime);
    }

    @Test
    public void testCreateScheduleWithUnauthorizedUser() {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("startTime", "2025-06-15 10:00:00");
        requestBody.put("endTime", "2025-06-15 12:00:00");

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.createSchedule(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only caregivers can create schedules", response.getBody().get("message"));
    }

    @Test
    public void testBookConsultationWithDateTime() {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("startTime", "2025-06-15 10:00:00");
        requestBody.put("endTime", "2025-06-15 12:00:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.bookConsultationWithDateTime("C001", "P001", startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.bookConsultation(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation booked successfully", response.getBody().get("message"));
        verify(schedulingService).bookConsultationWithDateTime("C001", "P001", startTime, endTime);
    }

    @Test
    public void testBookConsultationWithDateTimeFailed() {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("startTime", "2025-06-15 10:00:00");
        requestBody.put("endTime", "2025-06-15 12:00:00");

        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);

        when(schedulingService.bookConsultationWithDateTime("C001", "P001", startTime, endTime)).thenReturn(false);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.bookConsultation(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to book consultation", response.getBody().get("message"));
        verify(schedulingService).bookConsultationWithDateTime("C001", "P001", startTime, endTime);
    }

    @Test
    public void testBookConsultationWithUnauthorizedUser() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("startTime", "2025-06-15 10:00:00");
        requestBody.put("endTime", "2025-06-15 12:00:00");

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.bookConsultation(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Only patients can book consultations", response.getBody().get("message"));
    }

    @Test
    public void testAcceptConsultationWithDateTime() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15 10:00:00";
        String endTimeStr = "2025-06-15 12:00:00";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        when(schedulingService.acceptConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.acceptConsultation(
                caregiverId, pacilianId, startTimeStr, endTimeStr, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation accepted successfully", response.getBody().get("message"));
        verify(schedulingService).acceptConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
    }

    @Test
    public void testAcceptConsultationWithWrongCaregiver() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C002"; // Different from the authenticated caregiver
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15 10:00:00";
        String endTimeStr = "2025-06-15 12:00:00";

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.acceptConsultation(
                caregiverId, pacilianId, startTimeStr, endTimeStr, authentication);

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("You can only accept your own consultations", response.getBody().get("message"));
    }

    @Test
    public void testRejectConsultationWithDateTime() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        String pacilianId = "P001";
        String startTimeStr = "2025-06-15 10:00:00";
        String endTimeStr = "2025-06-15 12:00:00";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        when(schedulingService.rejectConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.rejectConsultation(
                caregiverId, pacilianId, startTimeStr, endTimeStr, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Consultation rejected successfully", response.getBody().get("message"));
        verify(schedulingService).rejectConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
    }

    @Test
    public void testGetCaregiverSchedules() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        List<Map<String, Object>> schedules = new ArrayList<>();

        when(schedulingService.getCaregiverSchedulesFormatted(caregiverId)).thenReturn(schedules);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.getCaregiverSchedules(caregiverId, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(schedules, response.getBody().get("schedules"));
        verify(schedulingService).getCaregiverSchedulesFormatted(caregiverId);
    }

    @Test
    public void testGetCaregiverSchedulesAsPatient() {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        String caregiverId = "C001";
        List<Map<String, Object>> schedules = new ArrayList<>();

        when(schedulingService.getCaregiverSchedulesFormatted(caregiverId)).thenReturn(schedules);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.getCaregiverSchedules(caregiverId, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(schedules, response.getBody().get("schedules"));
        verify(schedulingService).getCaregiverSchedulesFormatted(caregiverId);
    }

    @Test
    public void testCaregiverCannotViewOtherCaregiverSchedules() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String otherCaregiverId = "C002"; // Different from authenticated caregiver

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.getCaregiverSchedules(otherCaregiverId, authentication);

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("You can only view your own schedules", response.getBody().get("message"));
    }

    @Test
    public void testGetCaregiverConsultations() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        List<Consultation> consultations = new ArrayList<>();

        when(schedulingService.getCaregiverConsultations(caregiverId)).thenReturn(consultations);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.getCaregiverConsultations(
                caregiverId, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(schedulingService).getCaregiverConsultations(caregiverId);
    }

    @Test
    public void testGetPatientConsultations() {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        String pacilianId = "P001";
        List<Consultation> consultations = new ArrayList<>();

        when(schedulingService.getPatientConsultations(pacilianId)).thenReturn(consultations);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.getPatientConsultations(
                pacilianId, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(consultations, response.getBody().get("consultations"));
        verify(schedulingService).getPatientConsultations(pacilianId);
    }

    @Test
    public void testDeleteScheduleWithDateTime() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        String caregiverId = "C001";
        String startTimeStr = "2025-06-15 10:00:00";
        String endTimeStr = "2025-06-15 12:00:00";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        when(schedulingService.deleteScheduleWithDateTime(caregiverId, startTime, endTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.deleteSchedule(
                caregiverId, startTimeStr, endTimeStr, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule deleted successfully", response.getBody().get("message"));
        verify(schedulingService).deleteScheduleWithDateTime(caregiverId, startTime, endTime);
    }

    @Test
    public void testModifyScheduleWithDateTime() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C001");
        requestBody.put("oldStartTime", "2025-06-15 10:00:00");
        requestBody.put("oldEndTime", "2025-06-15 12:00:00");
        requestBody.put("newStartTime", "2025-06-16 14:00:00");
        requestBody.put("newEndTime", "2025-06-16 16:00:00");

        LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
        LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
        LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);

        when(schedulingService.modifyScheduleWithDateTime(
                "C001", oldStartTime, oldEndTime, newStartTime, newEndTime)).thenReturn(true);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.modifySchedule(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Schedule modified successfully", response.getBody().get("message"));
        verify(schedulingService).modifyScheduleWithDateTime(
                "C001", oldStartTime, oldEndTime, newStartTime, newEndTime);
    }

    @Test
    public void testModifyScheduleWithWrongCaregiver() {
        // Setup
        String email = "caregiver@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(caregiverUser);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("caregiverId", "C002"); // Different from authenticated user
        requestBody.put("oldStartTime", "2025-06-15 10:00:00");
        requestBody.put("oldEndTime", "2025-06-15 12:00:00");
        requestBody.put("newStartTime", "2025-06-16T14:00");
        requestBody.put("newEndTime", "2025-06-16T16:00");

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.modifySchedule(requestBody, authentication);

        // Verify
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("You can only modify your own schedules", response.getBody().get("message"));
    }

    @Test
    public void testFindAvailableCaregivers() {
        // Setup
        String email = "pacilian@example.com";
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(pacilianUser);
        
        String startTimeStr = "2025-06-15 10:00:00";
        String endTimeStr = "2025-06-15 12:00:00";
        String specialty = "Cardiology";

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        List<Map<String, Object>> caregivers = new ArrayList<>();
        Map<String, Object> caregiver = new HashMap<>();
        caregiver.put("id", "C001");
        caregiver.put("name", "Dr. Smith");
        caregivers.add(caregiver);

        when(schedulingService.findAvailableCaregivers(startTime, endTime, specialty)).thenReturn(caregivers);

        // Execute
        ResponseEntity<Map<String, Object>> response = controller.findAvailableCaregivers(
                startTimeStr, endTimeStr, specialty, authentication);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(caregivers, response.getBody().get("caregivers"));
        verify(schedulingService).findAvailableCaregivers(startTime, endTime, specialty);
    }
}