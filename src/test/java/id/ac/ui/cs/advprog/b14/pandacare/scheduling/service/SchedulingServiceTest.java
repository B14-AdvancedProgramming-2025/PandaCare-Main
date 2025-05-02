package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void testCreateSchedule() {
        when(context.createSchedule("C001", "Monday 10:00-12:00")).thenReturn(true);
        
        boolean result = service.createSchedule("C001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(context).createSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testBookConsultation() {
        when(context.bookConsultation("C001", "P001", "Monday 10:00-12:00")).thenReturn(true);
        
        boolean result = service.bookConsultation("C001", "P001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(context).bookConsultation("C001", "P001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testAcceptConsultation() {
        when(context.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED")).thenReturn(true);
        
        boolean result = service.acceptConsultation("C001", "P001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(context).updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
    }
    
    @Test
    public void testRejectConsultation() {
        when(context.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "REJECTED")).thenReturn(true);
        
        boolean result = service.rejectConsultation("C001", "P001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(context).updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "REJECTED");
    }
    
    @Test
    public void testModifyConsultation() {
        when(context.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "MODIFIED")).thenReturn(true);
        
        boolean result = service.modifyConsultation("C001", "P001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(context).updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "MODIFIED");
    }
}