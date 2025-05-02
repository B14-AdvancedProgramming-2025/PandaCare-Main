package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JpaConsultationRepositoryTest {

    @Mock
    private ConsultationJpaRepository jpaRepository;
    
    private JpaConsultationRepository repository;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaConsultationRepository(jpaRepository);
    }
    
    @Test
    public void testSaveConsultation() {
        when(jpaRepository.save(any(Consultation.class))).thenReturn(
                new Consultation("CONS1", "C001", "P001", "Monday 10:00-12:00", "BOOKED")
        );
        
        boolean result = repository.saveConsultation("C001", "P001", "Monday 10:00-12:00", "BOOKED");
        
        assertTrue(result);
        verify(jpaRepository).save(any(Consultation.class));
    }
    
    @Test
    public void testUpdateStatus() {
        when(jpaRepository.findByCaregiverIdAndPacilianIdAndScheduleTime(
                "C001", "P001", "Monday 10:00-12:00")
        ).thenReturn(new Consultation("CONS1", "C001", "P001", "Monday 10:00-12:00", "BOOKED"));
        
        when(jpaRepository.save(any(Consultation.class))).thenReturn(
                new Consultation("CONS1", "C001", "P001", "Monday 10:00-12:00", "ACCEPTED")
        );
        
        boolean result = repository.updateStatus("C001", "P001", "Monday 10:00-12:00", "ACCEPTED");
        
        assertTrue(result);
        verify(jpaRepository).findByCaregiverIdAndPacilianIdAndScheduleTime(
                "C001", "P001", "Monday 10:00-12:00");
        verify(jpaRepository).save(any(Consultation.class));
    }
}