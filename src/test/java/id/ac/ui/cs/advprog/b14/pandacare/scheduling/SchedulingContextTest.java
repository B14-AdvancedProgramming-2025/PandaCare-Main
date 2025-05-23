package id.ac.ui.cs.advprog.b14.pandacare.scheduling;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void testCreateSchedule() {
        when(mockStrategy.createSchedule("C001", "Monday 10:00-12:00")).thenReturn(true);
        
        boolean result = context.createSchedule("C001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(mockStrategy).createSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testBookConsultation() {
        when(mockStrategy.bookConsultation("C001", "P001", "Monday 10:00-12:00")).thenReturn(true);
        
        boolean result = context.bookConsultation("C001", "P001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(mockStrategy).bookConsultation("C001", "P001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testUpdateConsultationStatus() {
        when(mockStrategy.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED")).thenReturn(true);
        
        boolean result = context.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
        
        assertTrue(result);
        verify(mockStrategy).updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
    }
}