package id.ac.ui.cs.advprog.b14.pandacare.scheduling;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SchedulingContextTest {

    @Mock
    private SchedulingStrategy mockStrategy;
    
    private SchedulingContext context;

    private CaregiverRepositoryAdapter caregiverAdapter;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        context = new SchedulingContext(caregiverAdapter);
        context.setStrategy(mockStrategy);
    }
    
    @Test
    public void testCreateScheduleWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(mockStrategy.createScheduleWithDateTime(eq("C001"), eq(startTime), eq(endTime))).thenReturn(true);
        
        // Execute
        boolean result = context.createScheduleWithDateTime("C001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(mockStrategy).createScheduleWithDateTime(eq("C001"), eq(startTime), eq(endTime));
    }
    
    @Test
    public void testBookConsultationWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(mockStrategy.bookConsultationWithDateTime(
            eq("C001"), eq("P001"), eq(startTime), eq(endTime))).thenReturn(true);
        
        // Execute
        boolean result = context.bookConsultationWithDateTime("C001", "P001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(mockStrategy).bookConsultationWithDateTime(eq("C001"), eq("P001"), eq(startTime), eq(endTime));
    }
}