package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultationRepositoryTest {

    @Mock
    private ConsultationRepository repository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByCaregiverIdAndDateRange() {
        // Setup test data
        Consultation consultation = new Consultation();
        consultation.setId("CONS1");
        consultation.setCaregiverId("C001");
        consultation.setPacilianId("P001");
        consultation.setStartTime(LocalDateTime.of(2025, 6, 15, 10, 0));
        consultation.setEndTime(LocalDateTime.of(2025, 6, 15, 12, 0));
        consultation.setStatus("BOOKED");
        
        LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(repository.findByCaregiverIdAndStartTimeBetween("C001", start, end))
            .thenReturn(Arrays.asList(consultation));
        
        // Execute the method being tested
        List<Consultation> results = repository.findByCaregiverIdAndStartTimeBetween("C001", start, end);
        
        // Verify the results
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(consultation, results.get(0));
        verify(repository).findByCaregiverIdAndStartTimeBetween("C001", start, end);
    }
    
    @Test
    public void testFindByCaregiverIdAndPacilianIdAndDateRange() {
        Consultation consultation = new Consultation();
        consultation.setId("CONS1");
        consultation.setCaregiverId("C001");
        consultation.setPacilianId("P001");
        consultation.setStartTime(LocalDateTime.of(2025, 6, 15, 10, 0));
        consultation.setEndTime(LocalDateTime.of(2025, 6, 15, 12, 0));
        consultation.setStatus("BOOKED");
        
        LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(repository.findByCaregiverIdAndPacilianIdAndStartTimeAndEndTime("C001", "P001", start, end))
            .thenReturn(consultation);
        
        // Execute
        Consultation result = repository.findByCaregiverIdAndPacilianIdAndStartTimeAndEndTime("C001", "P001", start, end);
        
        // Verify
        assertNotNull(result);
        assertEquals(consultation, result);
        verify(repository).findByCaregiverIdAndPacilianIdAndStartTimeAndEndTime("C001", "P001", start, end);
    }
}