package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    public void testCreateSchedule() {
        when(scheduleRepository.saveSchedule(any(), any())).thenReturn(true);
        
        boolean result = strategy.createSchedule("C001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(scheduleRepository).saveSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testBookConsultation() {
        // Setup
        when(scheduleRepository.isScheduleAvailable("C001", "Monday 10:00-12:00")).thenReturn(true);
        when(consultationService.saveConsultation("C001", "P001", "Monday 10:00-12:00", "PENDING")).thenReturn(true);
        
        // Execute
        boolean result = strategy.bookConsultation("C001", "P001", "Monday 10:00-12:00");
        
        // Verify
        assertTrue(result);
        verify(scheduleRepository).isScheduleAvailable("C001", "Monday 10:00-12:00");
        verify(consultationService).saveConsultation("C001", "P001", "Monday 10:00-12:00", "PENDING");
    }
    
    @Test
    public void testBookConsultationWhenScheduleNotAvailable() {
        // Setup
        when(scheduleRepository.isScheduleAvailable("C001", "Monday 10:00-12:00")).thenReturn(false);
        
        // Execute
        boolean result = strategy.bookConsultation("C001", "P001", "Monday 10:00-12:00");
        
        // Verify
        assertFalse(result);
        verify(scheduleRepository).isScheduleAvailable("C001", "Monday 10:00-12:00");
        verify(consultationService, never()).saveConsultation(any(), any(), any(), any());
    }

    @Test
    public void testUpdateConsultationStatus() {
        when(consultationService.updateStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED")).thenReturn(true);
        
        boolean result = strategy.updateConsultationStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
        
        assertTrue(result);
        verify(consultationService).updateStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
    }
    
    @Test
    public void testGetCaregiverConsultations() {
        List<Consultation> consultations = new ArrayList<>();
        when(consultationService.findConsultationsByCaregiverId("C001")).thenReturn(consultations);
        
        List<Consultation> result = strategy.getCaregiverConsultations("C001");
        
        assertEquals(consultations, result);
        verify(consultationService).findConsultationsByCaregiverId("C001");
    }
    
    @Test
    public void testGetPatientConsultations() {
        List<Consultation> consultations = new ArrayList<>();
        when(consultationService.findConsultationsByPacilianId("P001")).thenReturn(consultations);
        
        List<Consultation> result = strategy.getPatientConsultations("P001");
        
        assertEquals(consultations, result);
        verify(consultationService).findConsultationsByPacilianId("P001");
    }
}