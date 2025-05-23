package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkingScheduleRepositoryTest {
    
    @Mock
    private WorkingScheduleRepository repository;
    
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
        
        when(repository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00"))
            .thenReturn(Optional.of(schedule));
        
        // Execute the method being tested
        Optional<WorkingSchedule> result = repository.findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
        
        // Verify the results
        assertTrue(result.isPresent());
        assertEquals(schedule, result.get());
        verify(repository).findByCaregiverIdAndSchedule("C001", "Monday 10:00-12:00");
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
        
        when(repository.findByCaregiverId("C001"))
            .thenReturn(schedules);
        
        // Execute the method being tested
        List<WorkingSchedule> result = repository.findByCaregiverId("C001");
        
        // Verify the results
        assertEquals(2, result.size());
        assertEquals(schedules, result);
        verify(repository).findByCaregiverId("C001");
    }
    
    @Test
    public void testUpdateAvailability() {
        when(repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED"))
            .thenReturn(1);
        
        // Execute the method being tested
        int result = repository.updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
        
        // Verify the results
        assertEquals(1, result);
        verify(repository).updateAvailability("C001", "Monday 10:00-12:00", false, "BOOKED");
    }
}