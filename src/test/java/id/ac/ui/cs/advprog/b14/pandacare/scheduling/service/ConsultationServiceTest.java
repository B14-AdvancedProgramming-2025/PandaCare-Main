package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;
    
    @Mock
    private WorkingScheduleRepository workingScheduleRepository;
    
    private ConsultationService service;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new ConsultationService(consultationRepository, workingScheduleRepository);
    }
    
    @Test
    public void testSaveConsultation() {
        // Setup
        when(consultationRepository.save(any(Consultation.class))).thenReturn(new Consultation());
        when(workingScheduleRepository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED")).thenReturn(1);
        
        // Execute
        boolean result = service.saveConsultation("C001", "P001", "Monday 10:00-12:00", "PENDING");
        
        // Verify
        assertTrue(result);
        verify(consultationRepository).save(any(Consultation.class));
        verify(workingScheduleRepository).updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
    }
    
    @Test
    public void testUpdateStatusToAccepted() {
        // Setup
        Consultation consultation = new Consultation("1", "C001", "P001", "Monday 10:00-12:00", "PENDING");
        when(consultationRepository.findByCaregiverIdAndPacilianIdAndScheduleTime("C001", "P001", "Monday 10:00-12:00"))
            .thenReturn(consultation);
        when(consultationRepository.save(consultation)).thenReturn(consultation);
        
        // Execute
        boolean result = service.updateStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
        
        // Verify
        assertTrue(result);
        assertEquals("ACCEPTED", consultation.getStatus());
        verify(consultationRepository).save(consultation);
        verify(workingScheduleRepository, never()).updateAvailability(any(), any(), anyBoolean(), any());
    }
    
    @Test
    public void testUpdateStatusToRejected() {
        // Setup
        Consultation consultation = new Consultation("1", "C001", "P001", "Monday 10:00-12:00", "PENDING");
        when(consultationRepository.findByCaregiverIdAndPacilianIdAndScheduleTime("C001", "P001", "Monday 10:00-12:00"))
            .thenReturn(consultation);
        when(consultationRepository.save(consultation)).thenReturn(consultation);
        when(workingScheduleRepository.updateAvailability("C001", "Monday 10:00-12:00", true, "AVAILABLE")).thenReturn(1);
        
        // Execute
        boolean result = service.updateStatus("C001", "P001", "Monday 10:00-12:00", "REJECTED");
        
        // Verify
        assertTrue(result);
        assertEquals("REJECTED", consultation.getStatus());
        verify(consultationRepository).save(consultation);
        verify(workingScheduleRepository).updateAvailability("C001", "Monday 10:00-12:00", true, "AVAILABLE");
    }
    
    @Test
    public void testFindConsultationsByCaregiverId() {
        // Setup
        List<Consultation> consultations = new ArrayList<>();
        consultations.add(new Consultation("1", "C001", "P001", "Monday 10:00-12:00", "PENDING"));
        when(consultationRepository.findByCaregiverId("C001")).thenReturn(consultations);
        
        // Execute
        List<Consultation> result = service.findConsultationsByCaregiverId("C001");
        
        // Verify
        assertEquals(consultations, result);
        verify(consultationRepository).findByCaregiverId("C001");
    }
    
    @Test
    public void testFindConsultationsByPacilianId() {
        // Setup
        List<Consultation> consultations = new ArrayList<>();
        consultations.add(new Consultation("1", "C001", "P001", "Monday 10:00-12:00", "PENDING"));
        when(consultationRepository.findByPacilianId("P001")).thenReturn(consultations);
        
        // Execute
        List<Consultation> result = service.findConsultationsByPacilianId("P001");
        
        // Verify
        assertEquals(consultations, result);
        verify(consultationRepository).findByPacilianId("P001");
    }
}