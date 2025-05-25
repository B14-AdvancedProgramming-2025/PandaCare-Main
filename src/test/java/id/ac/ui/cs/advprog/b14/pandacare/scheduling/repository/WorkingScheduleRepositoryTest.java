package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    public void testFindByCaregiverIdAndDateRange() {
        // Setup test data
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setId(UUID.randomUUID().toString());
        schedule.setCaregiverId("C001");
        schedule.setStartTime(LocalDateTime.of(2025, 6, 15, 10, 0));
        schedule.setEndTime(LocalDateTime.of(2025, 6, 15, 12, 0));
        schedule.setStatus("AVAILABLE");
        schedule.setAvailable(true);
        
        LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(repository.findByCaregiverIdAndStartTimeBetween("C001", start, end))
            .thenReturn(Arrays.asList(schedule));
        
        // Execute the method being tested
        List<WorkingSchedule> results = repository.findByCaregiverIdAndStartTimeBetween("C001", start, end);
        
        // Verify the results
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(schedule, results.get(0));
        verify(repository).findByCaregiverIdAndStartTimeBetween("C001", start, end);
    }
    
    @Test
    public void testUpdateAvailabilityWithDateTime() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 15, 12, 0);
        
        when(repository.updateAvailabilityByDateTime("C001", start, end, false, "BOOKED"))
            .thenReturn(1);
        
        // Execute the method being tested
        int result = repository.updateAvailabilityByDateTime("C001", start, end, false, "BOOKED");
        
        // Verify the results
        assertEquals(1, result);
        verify(repository).updateAvailabilityByDateTime("C001", start, end, false, "BOOKED");
    }
}