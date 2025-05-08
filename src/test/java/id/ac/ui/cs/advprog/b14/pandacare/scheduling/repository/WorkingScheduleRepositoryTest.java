package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorkingScheduleRepositoryTest {
    
    @Mock
    private WorkingScheduleJpaRepository jpaRepository;
    
    @InjectMocks
    private JpaWorkingScheduleRepository repository;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testFindByCaregiverIdAndSchedule() {
        // Setup test data
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setId(1L);
        schedule.setCaregiverId("C001");
        schedule.setSchedule("Monday 10:00-12:00");
        schedule.setStatus("AVAILABLE");
        schedule.setAvailable(true);
        
        when(jpaRepository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00"))
            .thenReturn(Optional.of(schedule));
        
        // Execute the method being tested
        Optional<WorkingSchedule> result = repository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
        
        // Verify the results
        assertTrue(result.isPresent());
        assertEquals(schedule, result.get());
        verify(jpaRepository).findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testFindByCaregiverIdAndScheduleWhenExceptionOccurs() {
        when(jpaRepository.findByCaregiverIdAndSchedule(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));
        
        Optional<WorkingSchedule> result = repository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
        
        assertFalse(result.isPresent());
    }
    
    @Test
    public void testFindByCaregiverId() {
        // Setup test data
        WorkingSchedule schedule1 = new WorkingSchedule();
        schedule1.setId(1L);
        schedule1.setCaregiverId("C001");
        schedule1.setSchedule("Monday 10:00-12:00");
        schedule1.setStatus("AVAILABLE");
        schedule1.setAvailable(true);
        
        WorkingSchedule schedule2 = new WorkingSchedule();
        schedule2.setId(2L);
        schedule2.setCaregiverId("C001");
        schedule2.setSchedule("Tuesday 14:00-16:00");
        schedule2.setStatus("AVAILABLE");
        schedule2.setAvailable(true);
        
        List<WorkingSchedule> schedules = Arrays.asList(schedule1, schedule2);
        
        when(jpaRepository.findByCaregiverId("C001"))
            .thenReturn(schedules);
        
        // Execute the method being tested
        List<WorkingSchedule> result = repository.findByCaregiverId("C001");
        
        // Verify the results
        assertEquals(2, result.size());
        assertEquals(schedules, result);
        verify(jpaRepository).findByCaregiverId("C001");
    }
    
    @Test
    public void testFindByCaregiverIdWhenExceptionOccurs() {
        when(jpaRepository.findByCaregiverId(anyString()))
            .thenThrow(new RuntimeException("Database error"));
        
        List<WorkingSchedule> result = repository.findByCaregiverId("C001");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testUpdateAvailability() {
        when(jpaRepository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED"))
            .thenReturn(1);
        
        // Execute the method being tested
        boolean result = repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
        
        // Verify the results
        assertTrue(result);
        verify(jpaRepository).updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
    }
    
    @Test
    public void testUpdateAvailabilityWhenNoRowsAffected() {
        when(jpaRepository.updateAvailability(anyString(), anyString(), anyBoolean(), anyString()))
            .thenReturn(0);
        
        boolean result = repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
        
        assertFalse(result);
    }
    
    @Test
    public void testUpdateAvailabilityWhenExceptionOccurs() {
        when(jpaRepository.updateAvailability(anyString(), anyString(), anyBoolean(), anyString()))
            .thenThrow(new RuntimeException("Database error"));
        
        boolean result = repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
        
        assertFalse(result);
    }
    
    @Test
    public void testSave() {
        // Setup test data
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setCaregiverId("C001");
        schedule.setSchedule("Friday 10:00-12:00");
        schedule.setStatus("AVAILABLE");
        schedule.setAvailable(true);
        
        WorkingSchedule savedSchedule = new WorkingSchedule();
        savedSchedule.setId(1L);
        savedSchedule.setCaregiverId("C001");
        savedSchedule.setSchedule("Friday 10:00-12:00");
        savedSchedule.setStatus("AVAILABLE");
        savedSchedule.setAvailable(true);
        
        when(jpaRepository.save(schedule))
            .thenReturn(savedSchedule);
        
        // Execute the method being tested
        WorkingSchedule result = repository.save(schedule);
        
        // Verify the results
        assertNotNull(result);
        assertEquals(savedSchedule, result);
        verify(jpaRepository).save(schedule);
    }
    
    @Test
    public void testSaveWhenExceptionOccurs() {
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setCaregiverId("C001");
        schedule.setSchedule("Friday 10:00-12:00");
        schedule.setStatus("AVAILABLE");
        schedule.setAvailable(true);
        
        when(jpaRepository.save(any(WorkingSchedule.class)))
            .thenThrow(new RuntimeException("Database error"));
        
        WorkingSchedule result = repository.save(schedule);
        
        assertNull(result);
    }
}