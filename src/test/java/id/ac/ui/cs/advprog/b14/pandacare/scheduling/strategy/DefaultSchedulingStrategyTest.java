package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultSchedulingStrategyTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    
    @Mock
    private ConsultationRepository consultationRepository;
    
    private DefaultSchedulingStrategy strategy;

    private WorkingScheduleRepository workingScheduleRepository;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        strategy = new DefaultSchedulingStrategy(scheduleRepository, consultationRepository, workingScheduleRepository);
    }
    
    @Test
    public void testCreateSchedule() {
        when(scheduleRepository.saveSchedule(any(), any())).thenReturn(true);
        
        boolean result = strategy.createSchedule("C001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(scheduleRepository).saveSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testBookConsultation() {
        when(scheduleRepository.isScheduleAvailable(any(), any())).thenReturn(true);
        when(consultationRepository.saveConsultation(any(), any(), any(), any())).thenReturn(true);
        
        boolean result = strategy.bookConsultation("C001", "P001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(scheduleRepository).isScheduleAvailable("C001", "Monday 10:00-12:00");
        verify(consultationRepository).saveConsultation("C001", "P001", "Monday 10:00-12:00", "BOOKED");
    }
    
    @Test
    public void testUpdateConsultationStatus() {
        when(consultationRepository.updateStatus(any(), any(), any(), any())).thenReturn(true);
        
        boolean result = strategy.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
        
        assertTrue(result);
        verify(consultationRepository).updateStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
    }
}