package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
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
    
    @Mock
    private WorkingScheduleRepository workingScheduleRepository;
    
    private CaregiverScheduleRepository repository;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new CaregiverScheduleRepository(caregiverAdapter, workingScheduleRepository);
    }
    
    @Test
    public void testSaveSchedule() {
        // Create caregiver with empty schedule list
        Caregiver caregiver = new Caregiver(
                "doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        );
        
        // Setup mocks
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.of(caregiver));
        when(workingScheduleRepository.save(any(WorkingSchedule.class))).thenReturn(new WorkingSchedule());
        
        // Execute
        boolean result = repository.saveSchedule("C001", "Monday 10:00-12:00");
        
        // Verify
        assertTrue(result);
        verify(caregiverAdapter).findById("C001"); 
        // No longer saving to caregiver entity
        verify(caregiverAdapter, never()).save(any(Caregiver.class));
        verify(workingScheduleRepository).save(any(WorkingSchedule.class));
    }
    
    @Test
    public void testIsScheduleAvailable() {
        // Setup
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setCaregiverId("C001");
        schedule.setSchedule("Monday 10:00-12:00");
        schedule.setAvailable(true);
        
        when(workingScheduleRepository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00"))
            .thenReturn(Optional.of(schedule));
        
        // Execute
        boolean result = repository.isScheduleAvailable("C001", "Monday 10:00-12:00");
        
        // Verify
        assertTrue(result);
        verify(workingScheduleRepository).findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
    }

    @Test
    public void testSaveScheduleOverlap() {
        // Setup - caregiver with existing overlapping schedule
        List<String> schedules = new ArrayList<>();
        schedules.add("Monday 08:00-12:00");
        
        Caregiver caregiver = new Caregiver(
                "doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", schedules
        );
        
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.of(caregiver));
        
        // Execute - try to add overlapping schedule
        boolean result = repository.saveSchedule("C001", "Monday 10:00-14:00");

        // Verify - should fail due to overlap
        assertFalse(result);
        verify(caregiverAdapter).findById("C001");
        verify(caregiverAdapter, never()).save(any(Caregiver.class));
        verify(workingScheduleRepository, never()).save(any(WorkingSchedule.class));
    }

    @Test
    public void testSaveScheduleWithUnavailableCaregiver() {
        // Setup - caregiver not found
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.empty());
        
        // Execute
        boolean result = repository.saveSchedule("C001", "Monday 10:00-12:00");
        
        // Verify
        assertFalse(result);
        verify(caregiverAdapter).findById("C001");
        verify(caregiverAdapter, never()).save(any(Caregiver.class));
        verify(workingScheduleRepository, never()).save(any(WorkingSchedule.class));
    }
}