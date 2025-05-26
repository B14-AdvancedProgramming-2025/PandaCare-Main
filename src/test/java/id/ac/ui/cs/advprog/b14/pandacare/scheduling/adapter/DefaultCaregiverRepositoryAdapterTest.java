package id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.SchedulingCaregiverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultCaregiverRepositoryAdapterTest {

    @Mock
    private SchedulingCaregiverRepository caregiverRepository;
    
    private DefaultCaregiverRepositoryAdapter adapter;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        adapter = new DefaultCaregiverRepositoryAdapter(caregiverRepository);
    }
    
    @Test
    public void testFindById() {
        Caregiver caregiver = new Caregiver(
                "1","doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        );
        
        when(caregiverRepository.findById("C001")).thenReturn(Optional.of(caregiver));
        
        Optional<Caregiver> result = adapter.findById("C001");
        
        assertTrue(result.isPresent());
        assertEquals(caregiver, result.get());
        verify(caregiverRepository).findById("C001");
    }
    
    @Test
    public void testSave() {
        Caregiver caregiver = new Caregiver(
                "1","doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        );
        
        when(caregiverRepository.save(caregiver)).thenReturn(caregiver);
        
        Caregiver result = adapter.save(caregiver);
        
        assertEquals(caregiver, result);
        verify(caregiverRepository).save(caregiver);
    }

    @Test
    public void testFindAll() {
        List<Caregiver> caregivers = new ArrayList<>();
        caregivers.add(new Caregiver(
                "1","doctor1@example.com", "password", "Dr. John", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        ));
        caregivers.add(new Caregiver(
                "2","doctor2@example.com", "password", "Dr. Jane", 
                "987654321", "456 Example St", "0987654321", 
                "Pediatrics", new ArrayList<>()
        ));
        
        when(caregiverRepository.findAll()).thenReturn(caregivers);
        
        List<Caregiver> result = adapter.findAll();
        
        assertEquals(2, result.size());
        verify(caregiverRepository).findAll();
    }
}