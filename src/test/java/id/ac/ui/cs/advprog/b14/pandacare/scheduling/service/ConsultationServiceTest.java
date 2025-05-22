package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    public void testSaveConsultationWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(consultationRepository.save(any(Consultation.class))).thenReturn(new Consultation());
        when(workingScheduleRepository.updateAvailabilityByDateTime(
            eq("C001"), eq(startTime), eq(endTime), eq(false), eq("BOOKED")))
            .thenReturn(1);
        
        // Execute
        boolean result = service.saveConsultationWithDateTime("C001", "P001", startTime, endTime, "PENDING");
        
        // Verify
        assertTrue(result);
        verify(consultationRepository).save(any(Consultation.class));
        verify(workingScheduleRepository).updateAvailabilityByDateTime(
            eq("C001"), eq(startTime), eq(endTime), eq(false), eq("BOOKED"));
    }
    
    @Test
    public void testUpdateStatusWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        Consultation consultation = new Consultation("1", "C001", "P001", startTime, endTime, "PENDING");
        
        when(consultationRepository.findByCaregiverIdAndPacilianIdAndStartTimeAndEndTime(
            "C001", "P001", startTime, endTime))
            .thenReturn(consultation);
        when(consultationRepository.save(consultation)).thenReturn(consultation);
        
        // Execute
        boolean result = service.updateStatusWithDateTime(
            "C001", "P001", startTime, endTime, "ACCEPTED");
        
        // Verify
        assertTrue(result);
        assertEquals("ACCEPTED", consultation.getStatus());
        verify(consultationRepository).save(consultation);
    }
}