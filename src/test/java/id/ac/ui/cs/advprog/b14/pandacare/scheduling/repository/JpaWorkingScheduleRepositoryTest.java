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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JpaWorkingScheduleRepositoryTest {
    
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
        WorkingSchedule schedule = new WorkingSchedule(1L, "C001", "Monday 10:00-12:00", "AVAILABLE", true);
        
        when(jpaRepository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00"))
            .thenReturn(Optional.of(schedule));
        
        Optional<WorkingSchedule> result = repository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
        
        assertTrue(result.isPresent());
        assertEquals(schedule, result.get());
        verify(jpaRepository).findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
    }
    
    @Test
    public void testFindByCaregiverId() {
        WorkingSchedule schedule1 = new WorkingSchedule(1L, "C001", "Monday 10:00-12:00", "AVAILABLE", true);
        WorkingSchedule schedule2 = new WorkingSchedule(2L, "C001", "Tuesday 14:00-16:00", "BOOKED", false);
        
        when(jpaRepository.findByCaregiverId("C001"))
            .thenReturn(Arrays.asList(schedule1, schedule2));
        
        List<WorkingSchedule> result = repository.findByCaregiverId("C001");
        
        assertEquals(2, result.size());
        assertEquals(schedule1, result.get(0));
        assertEquals(schedule2, result.get(1));
        verify(jpaRepository).findByCaregiverId("C001");
    }
    
    @Test
    public void testUpdateAvailability() {
        when(jpaRepository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED"))
            .thenReturn(1);
        
        boolean result = repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
        
        assertTrue(result);
        verify(jpaRepository).updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
    }
    
    @Test
    public void testUpdateAvailabilityFailed() {
        when(jpaRepository.updateAvailability(anyString(), anyString(), anyBoolean(), anyString()))
            .thenReturn(0);
        
        boolean result = repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
        
        assertFalse(result);
        verify(jpaRepository).updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
    }
    
    @Test
    public void testSave() {
        WorkingSchedule schedule = new WorkingSchedule(1L, "C001", "Monday 10:00-12:00", "AVAILABLE", true);
        
        when(jpaRepository.save(schedule)).thenReturn(schedule);
        
        WorkingSchedule result = repository.save(schedule);
        
        assertEquals(schedule, result);
        verify(jpaRepository).save(schedule);
    }
}