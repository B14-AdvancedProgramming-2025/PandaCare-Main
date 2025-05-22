package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SchedulingServiceTest {

    @Mock
    private SchedulingContext context;
    
    @Mock
    private DefaultSchedulingStrategy defaultStrategy;
    
    private SchedulingService service;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SchedulingService(context, defaultStrategy);
    }
    
    @Test
    public void testCreateScheduleWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(context.createScheduleWithDateTime(eq("C001"), eq(startTime), eq(endTime))).thenReturn(true);
        
        // Execute
        boolean result = service.createScheduleWithDateTime("C001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(context).createScheduleWithDateTime(eq("C001"), eq(startTime), eq(endTime));
    }
    
    @Test
    public void testBookConsultationWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(context.bookConsultationWithDateTime(eq("C001"), eq("P001"), eq(startTime), eq(endTime))).thenReturn(true);
        
        // Execute
        boolean result = service.bookConsultationWithDateTime("C001", "P001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(context).bookConsultationWithDateTime(eq("C001"), eq("P001"), eq(startTime), eq(endTime));
    }
    
    @Test
    public void testAcceptConsultationWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(context.updateConsultationStatusWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime), eq("ACCEPTED"))).thenReturn(true);
        
        // Execute
        boolean result = service.acceptConsultationWithDateTime("C001", "P001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(context).updateConsultationStatusWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime), eq("ACCEPTED"));
    }
}