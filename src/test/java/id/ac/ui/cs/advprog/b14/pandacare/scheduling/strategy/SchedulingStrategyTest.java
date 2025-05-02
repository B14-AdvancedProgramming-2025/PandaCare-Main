package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;

public class SchedulingStrategyTest {
    
    @Test
    void timeBasedSchedulingStrategy_IsScheduleAvailable_ShouldReturnTrue_WhenNoConflict() {
        // Arrange
        TimeBasedSchedulingStrategy strategy = new TimeBasedSchedulingStrategy();
        List<String> existingSchedules = Arrays.asList(
            "2023-12-20 10:00-12:00"
        );
        
        // Act
        boolean result = strategy.isScheduleAvailable("2023-12-20 14:00-16:00", existingSchedules);
        
        // Assert
        assertTrue(result, "Schedule should be available when there's no conflict");
    }
    
    @Test
    void timeBasedSchedulingStrategy_IsScheduleAvailable_ShouldReturnFalse_WhenConflict() {
        // Arrange
        TimeBasedSchedulingStrategy strategy = new TimeBasedSchedulingStrategy();
        List<String> existingSchedules = Arrays.asList(
            "2023-12-20 10:00-12:00"
        );
        
        // Act
        boolean result = strategy.isScheduleAvailable("2023-12-20 11:00-13:00", existingSchedules);
        
        // Assert
        assertFalse(result, "Schedule should not be available when there's a conflict");
    }
    
    @Test
    void conflictAvoidanceSchedulingStrategy_IsScheduleAvailable_ShouldReturnTrue_WhenNoConflict() {
        // Arrange
        ConflictAvoidanceSchedulingStrategy strategy = new ConflictAvoidanceSchedulingStrategy();
        List<String> existingSchedules = Arrays.asList(
            "2023-12-20 10:00-12:00"
        );
        
        // Act
        boolean result = strategy.isScheduleAvailable("2023-12-20 14:00-16:00", existingSchedules);
        
        // Assert
        assertTrue(result, "Schedule should be available when there's no conflict");
    }
    
    @Test
    void conflictAvoidanceSchedulingStrategy_IsScheduleAvailable_ShouldReturnFalse_WhenInBufferZone() {
        // Arrange
        ConflictAvoidanceSchedulingStrategy strategy = new ConflictAvoidanceSchedulingStrategy();
        List<String> existingSchedules = Arrays.asList(
            "2023-12-20 10:00-12:00"
        );
        
        // Act
        // This is 20 minutes after the existing schedule, which should be in buffer zone
        boolean result = strategy.isScheduleAvailable("2023-12-20 12:20-14:00", existingSchedules);
        
        // Assert
        assertFalse(result, "Schedule should not be available when in buffer zone");
    }
}