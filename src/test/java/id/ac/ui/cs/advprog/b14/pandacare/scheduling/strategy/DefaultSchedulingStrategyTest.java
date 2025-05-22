package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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
    
    private DefaultSchedulingStrategy strategy;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        strategy = new DefaultSchedulingStrategy(
                scheduleRepository, 
                consultationRepository, 
                workingScheduleRepository,
                consultationService
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
}