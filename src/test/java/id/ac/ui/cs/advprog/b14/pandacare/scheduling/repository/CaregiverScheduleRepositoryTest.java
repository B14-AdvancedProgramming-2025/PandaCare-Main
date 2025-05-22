package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
    public void testSaveScheduleWithDateTime() {
        // Create caregiver with empty schedule list
        Caregiver caregiver = new Caregiver(
                "doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        );
        
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        
        // Setup mocks
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.of(caregiver));
        when(workingScheduleRepository.save(any(WorkingSchedule.class))).thenReturn(new WorkingSchedule());
        
        // Execute
        boolean result = repository.saveScheduleWithDateTime("C001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(caregiverAdapter).findById("C001");
        verify(workingScheduleRepository).save(argThat(schedule -> 
            schedule.getCaregiverId().equals("C001") && 
            schedule.getStartTime().equals(startTime) &&
            schedule.getEndTime().equals(endTime) &&
            schedule.getStatus().equals("AVAILABLE") &&
            schedule.isAvailable()
        ));
    }

    @Test
    public void testSaveScheduleWithDateTimeOverlap() {
        // Setup - caregiver with existing overlapping schedule
        Caregiver caregiver = new Caregiver(
                "doctor@example.com", "password", "Dr. Example", 
                "123456789", "123 Example St", "1234567890", 
                "General Practice", new ArrayList<>()
        );
        
        LocalDateTime existingStart = LocalDateTime.of(2025, 5, 15, 9, 0);
        LocalDateTime existingEnd = LocalDateTime.of(2025, 5, 15, 11, 0);
        
        // Create a list of existing schedules in DateTime format
        when(workingScheduleRepository.findByCaregiverId("C001")).thenReturn(
            List.of(new WorkingSchedule(1L, "C001", existingStart, existingEnd, "AVAILABLE", true))
        );
        
        when(caregiverAdapter.findById("C001")).thenReturn(Optional.of(caregiver));
        
        // Try to add overlapping schedule
        LocalDateTime newStart = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 5, 15, 12, 0);
        
        // Execute
        boolean result = repository.saveScheduleWithDateTime("C001", newStart, newEnd);
        
        // Verify - should fail due to overlap
        assertFalse(result);
        verify(caregiverAdapter).findById("C001");
        verify(workingScheduleRepository, never()).save(any(WorkingSchedule.class));
    }

    @Test
    public void testIsScheduleAvailableByDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setCaregiverId("C001");
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setAvailable(true);
        
        when(workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime))
            .thenReturn(Optional.of(schedule));
        
        // Execute
        boolean result = repository.isScheduleAvailableByDateTime("C001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(workingScheduleRepository).findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime);
    }

    @Test
    public void testIsScheduleAvailableByDateTime_NotAvailable() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setCaregiverId("C001");
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setAvailable(false);
        
        when(workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime))
            .thenReturn(Optional.of(schedule));
        
        // Execute
        boolean result = repository.isScheduleAvailableByDateTime("C001", startTime, endTime);
        
        // Verify
        assertFalse(result);
        verify(workingScheduleRepository).findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime);
    }

    @Test
    public void testDeleteScheduleWithDateTime() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        
        WorkingSchedule schedule = new WorkingSchedule();
        schedule.setId(1L);
        schedule.setCaregiverId("C001");
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        
        when(workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime))
            .thenReturn(Optional.of(schedule));
        
        // Execute
        boolean result = repository.deleteScheduleWithDateTime("C001", startTime, endTime);
        
        // Verify
        assertTrue(result);
        verify(workingScheduleRepository).findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime);
        verify(workingScheduleRepository).delete(schedule);
    }

    @Test
    public void testDeleteScheduleWithDateTime_NotFound() {
        // Setup
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        
        when(workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime))
            .thenReturn(Optional.empty());
        
        // Execute
        boolean result = repository.deleteScheduleWithDateTime("C001", startTime, endTime);
        
        // Verify
        assertFalse(result);
        verify(workingScheduleRepository).findByCaregiverIdAndStartTimeAndEndTime("C001", startTime, endTime);
        verify(workingScheduleRepository, never()).delete(any(WorkingSchedule.class));
    }

    @Test
    public void testModifyScheduleWithDateTime() {
        // Setup
        LocalDateTime oldStartTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime oldEndTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        LocalDateTime newStartTime = LocalDateTime.of(2025, 5, 16, 14, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2025, 5, 16, 16, 0);
        
        WorkingSchedule oldSchedule = new WorkingSchedule();
        oldSchedule.setId(1L);
        oldSchedule.setCaregiverId("C001");
        oldSchedule.setStartTime(oldStartTime);
        oldSchedule.setEndTime(oldEndTime);
        
        when(workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime("C001", oldStartTime, oldEndTime))
            .thenReturn(Optional.of(oldSchedule));
        when(workingScheduleRepository.findByCaregiverId("C001")).thenReturn(List.of(oldSchedule));
        when(workingScheduleRepository.save(any(WorkingSchedule.class))).thenReturn(oldSchedule);
        
        // Execute
        boolean result = repository.modifyScheduleWithDateTime("C001", oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        // Verify
        assertTrue(result);
        verify(workingScheduleRepository).findByCaregiverIdAndStartTimeAndEndTime("C001", oldStartTime, oldEndTime);
        verify(workingScheduleRepository).save(argThat(schedule -> 
            schedule.getId() == 1L &&
            schedule.getStartTime().equals(newStartTime) &&
            schedule.getEndTime().equals(newEndTime)
        ));
    }

    @Test
    public void testModifyScheduleWithDateTime_NotFound() {
        // Setup
        LocalDateTime oldStartTime = LocalDateTime.of(2025, 5, 15, 10, 0);
        LocalDateTime oldEndTime = LocalDateTime.of(2025, 5, 15, 12, 0);
        LocalDateTime newStartTime = LocalDateTime.of(2025, 5, 16, 14, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2025, 5, 16, 16, 0);
        
        when(workingScheduleRepository.findByCaregiverIdAndStartTimeAndEndTime("C001", oldStartTime, oldEndTime))
            .thenReturn(Optional.empty());
        
        // Execute
        boolean result = repository.modifyScheduleWithDateTime("C001", oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        // Verify
        assertFalse(result);
        verify(workingScheduleRepository).findByCaregiverIdAndStartTimeAndEndTime("C001", oldStartTime, oldEndTime);
        verify(workingScheduleRepository, never()).save(any(WorkingSchedule.class));
    }

    @Test
    public void testGetCaregiverSchedules() {
        // Setup
        List<WorkingSchedule> schedules = new ArrayList<>();
        
        // Create DateTime-based schedules
        WorkingSchedule schedule1 = new WorkingSchedule();
        schedule1.setCaregiverId("C001");
        schedule1.setStartTime(LocalDateTime.of(2025, 5, 15, 10, 0));
        schedule1.setEndTime(LocalDateTime.of(2025, 5, 15, 12, 0));
        schedule1.setAvailable(true);
        schedules.add(schedule1);
        
        WorkingSchedule schedule2 = new WorkingSchedule();
        schedule2.setCaregiverId("C001");
        schedule2.setStartTime(LocalDateTime.of(2025, 5, 16, 14, 0));
        schedule2.setEndTime(LocalDateTime.of(2025, 5, 16, 16, 0));
        schedule2.setAvailable(true);
        schedules.add(schedule2);
        
        when(workingScheduleRepository.findByCaregiverId("C001")).thenReturn(schedules);
        
        // Execute
        List<String> result = repository.getCaregiverSchedules("C001");
        
        // Verify
        assertEquals(2, result.size());
        // Format should match what's used in the repository implementation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String expected1 = schedule1.getStartTime().format(formatter) + "-" + schedule1.getEndTime().format(formatter);
        String expected2 = schedule2.getStartTime().format(formatter) + "-" + schedule2.getEndTime().format(formatter);
        
        assertTrue(result.contains(expected1));
        assertTrue(result.contains(expected2));
        verify(workingScheduleRepository).findByCaregiverId("C001");
    }
}