package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DefaultSchedulingStrategyTest {
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @Mock
    private ConsultationRepository consultationRepository;
    
    @Mock
    private WorkingScheduleRepository workingScheduleRepository;
    
    @Mock
    private ConsultationService consultationService;

    @Mock
    private CaregiverRepositoryAdapter caregiverAdapter;
    
    @Mock
    private ChatService chatService;
    
    private DefaultSchedulingStrategy strategy;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        strategy = new DefaultSchedulingStrategy(
                scheduleRepository, 
                consultationRepository, 
                workingScheduleRepository,
                consultationService,
                caregiverAdapter,
                chatService
        );
    }
    
    @Test
    public void testCreateScheduleWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(scheduleRepository.saveScheduleWithDateTime(eq("C001"), eq(startTime), eq(endTime))).thenReturn(true);
        
        // Execute
        boolean result = strategy.createScheduleWithDateTime("C001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(scheduleRepository).saveScheduleWithDateTime(eq("C001"), eq(startTime), eq(endTime));
    }
    
    @Test
    public void testBookConsultationWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(scheduleRepository.isScheduleAvailableByDateTime(eq("C001"), eq(startTime), eq(endTime))).thenReturn(true);
        when(consultationService.saveConsultationWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime), eq("PENDING"))).thenReturn(true);
        
        // Execute
        boolean result = strategy.bookConsultationWithDateTime("C001", "P001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(scheduleRepository).isScheduleAvailableByDateTime(eq("C001"), eq(startTime), eq(endTime));
        verify(consultationService).saveConsultationWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime), eq("PENDING"));
    }
    
    @Test
    public void testUpdateConsultationStatusWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(consultationService.updateStatusWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime), eq("ACCEPTED"))).thenReturn(true);
        
        // Execute
        boolean result = strategy.updateConsultationStatusWithDateTime(
            "C001", "P001", startTime, endTime, "ACCEPTED");
        
        // Verify
        assertTrue(result);
        verify(consultationService).updateStatusWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime), eq("ACCEPTED"));
    }
    
    @Test
    public void testFindAvailableCaregivers() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        String specialty = "Cardiology";
        
        // Create mock caregivers
        Caregiver mockCaregiver1 = mock(Caregiver.class);
        when(mockCaregiver1.getId()).thenReturn("C001");
        when(mockCaregiver1.getName()).thenReturn("Dr. John Doe");
        when(mockCaregiver1.getEmail()).thenReturn("john.doe@example.com");
        when(mockCaregiver1.getPhone()).thenReturn("1234567890");
        when(mockCaregiver1.getSpecialty()).thenReturn("Cardiology");
        
        Caregiver mockCaregiver2 = mock(Caregiver.class);
        when(mockCaregiver2.getId()).thenReturn("C002");
        when(mockCaregiver2.getName()).thenReturn("Dr. Jane Smith");
        when(mockCaregiver2.getEmail()).thenReturn("jane.smith@example.com");
        when(mockCaregiver2.getPhone()).thenReturn("0987654321");
        when(mockCaregiver2.getSpecialty()).thenReturn("Pediatrics");
        
        List<Caregiver> caregivers = new ArrayList<>();
        caregivers.add(mockCaregiver1);
        caregivers.add(mockCaregiver2);
        
        // Mock working schedule for caregiver1 (matching specialty)
        WorkingSchedule mockWorkingSchedule1 = mock(WorkingSchedule.class);
        when(mockWorkingSchedule1.isAvailable()).thenReturn(true);
        when(mockWorkingSchedule1.getStatus()).thenReturn("AVAILABLE");
        when(mockWorkingSchedule1.getStartTime()).thenReturn(startTime);
        when(mockWorkingSchedule1.getEndTime()).thenReturn(endTime);
        
        List<WorkingSchedule> availableSchedules1 = new ArrayList<>();
        availableSchedules1.add(mockWorkingSchedule1);
        
        when(caregiverAdapter.findAll()).thenReturn(caregivers);
        when(workingScheduleRepository.findByCaregiversWithOverlappingTimeSlots("C001", startTime, endTime))
                .thenReturn(availableSchedules1);
        when(workingScheduleRepository.findByCaregiversWithOverlappingTimeSlots("C002", startTime, endTime))
                .thenReturn(new ArrayList<>()); // No available schedules for caregiver2
        
        // Execute
        List<Map<String, Object>> result = strategy.findAvailableCaregivers(startTime, endTime, specialty);
        
        // Verify
        assertEquals(1, result.size());
        Map<String, Object> caregiverInfo = result.get(0);
        assertEquals("C001", caregiverInfo.get("id"));
        assertEquals("Dr. John Doe", caregiverInfo.get("name"));
        assertEquals("Cardiology", caregiverInfo.get("specialty"));
        verify(caregiverAdapter).findAll();
        verify(workingScheduleRepository).findByCaregiversWithOverlappingTimeSlots("C001", startTime, endTime);
    }

    @Test
    public void testFindAvailableCaregivers_NoSpecialty() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        String specialty = null; // No specialty filter
        
        // Create mock caregivers
        Caregiver mockCaregiver1 = mock(Caregiver.class);
        when(mockCaregiver1.getId()).thenReturn("C001");
        when(mockCaregiver1.getName()).thenReturn("Dr. John Doe");
        when(mockCaregiver1.getEmail()).thenReturn("john.doe@example.com");
        when(mockCaregiver1.getPhone()).thenReturn("1234567890");
        when(mockCaregiver1.getSpecialty()).thenReturn("Cardiology");
        
        Caregiver mockCaregiver2 = mock(Caregiver.class);
        when(mockCaregiver2.getId()).thenReturn("C002");
        when(mockCaregiver2.getName()).thenReturn("Dr. Jane Smith");
        when(mockCaregiver2.getEmail()).thenReturn("jane.smith@example.com");
        when(mockCaregiver2.getPhone()).thenReturn("0987654321");
        when(mockCaregiver2.getSpecialty()).thenReturn("Pediatrics");
        
        List<Caregiver> caregivers = new ArrayList<>();
        caregivers.add(mockCaregiver1);
        caregivers.add(mockCaregiver2);
        
        // Mock working schedules for both caregivers
        WorkingSchedule mockWorkingSchedule1 = mock(WorkingSchedule.class);
        when(mockWorkingSchedule1.isAvailable()).thenReturn(true);
        when(mockWorkingSchedule1.getStatus()).thenReturn("AVAILABLE");
        when(mockWorkingSchedule1.getStartTime()).thenReturn(startTime);
        when(mockWorkingSchedule1.getEndTime()).thenReturn(endTime);
        
        WorkingSchedule mockWorkingSchedule2 = mock(WorkingSchedule.class);
        when(mockWorkingSchedule2.isAvailable()).thenReturn(true);
        when(mockWorkingSchedule2.getStatus()).thenReturn("AVAILABLE");
        when(mockWorkingSchedule2.getStartTime()).thenReturn(startTime);
        when(mockWorkingSchedule2.getEndTime()).thenReturn(endTime);
        
        List<WorkingSchedule> availableSchedules1 = new ArrayList<>();
        availableSchedules1.add(mockWorkingSchedule1);
        
        List<WorkingSchedule> availableSchedules2 = new ArrayList<>();
        availableSchedules2.add(mockWorkingSchedule2);
        
        when(caregiverAdapter.findAll()).thenReturn(caregivers);
        when(workingScheduleRepository.findByCaregiversWithOverlappingTimeSlots("C001", startTime, endTime))
                .thenReturn(availableSchedules1);
        when(workingScheduleRepository.findByCaregiversWithOverlappingTimeSlots("C002", startTime, endTime))
                .thenReturn(availableSchedules2);
        
        // Execute
        List<Map<String, Object>> result = strategy.findAvailableCaregivers(startTime, endTime, specialty);
        
        // Verify
        assertEquals(2, result.size());
        verify(caregiverAdapter).findAll();
        verify(workingScheduleRepository).findByCaregiversWithOverlappingTimeSlots("C001", startTime, endTime);
        verify(workingScheduleRepository).findByCaregiversWithOverlappingTimeSlots("C002", startTime, endTime);
    }
}