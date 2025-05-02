package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CaregiverScheduleRepositoryTest {

    @Mock
    private CaregiverRepositoryAdapter caregiverAdapter;
    
    private CaregiverScheduleRepository repository;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new CaregiverScheduleRepository(caregiverAdapter);
    }
    
    @Test
    public void testSaveSchedule() {
        Caregiver caregiver = new Caregiver(
                "doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        );
        
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.of(caregiver));
        when(caregiverAdapter.save(any(Caregiver.class))).thenReturn(caregiver);
        
        boolean result = repository.saveSchedule("C001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(caregiverAdapter).findById("C001");
        verify(caregiverAdapter).save(caregiver);
        assertTrue(caregiver.getWorkingSchedule().contains("Monday 10:00-12:00"));
    }
    
    @Test
    public void testIsScheduleAvailable() {
        List<String> schedules = new ArrayList<>();
        schedules.add("Monday 10:00-12:00");
        
        Caregiver caregiver = new Caregiver(
                "doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", schedules
        );
        
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.of(caregiver));
        
        boolean result = repository.isScheduleAvailable("C001", "Monday 10:00-12:00");
        
        assertTrue(result);
        verify(caregiverAdapter).findById("C001");
    }
}