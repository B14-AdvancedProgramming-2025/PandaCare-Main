package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SchedulingServiceTest {

    @Mock
    private SchedulingContext context;

    private SchedulingService asyncService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        asyncService = new SchedulingService(context);
    }

    @Test
    public void testGetCaregiverConsultationsAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        List<Consultation> expectedConsultations = new ArrayList<>();
        when(context.getCaregiverConsultations(caregiverId)).thenReturn(expectedConsultations);

        // Execute
        CompletableFuture<List<Consultation>> future = asyncService.getCaregiverConsultationsAsync(caregiverId);
        List<Consultation> result = future.get(); // Block to get the result

        // Verify
        assertEquals(expectedConsultations, result);
        verify(context).getCaregiverConsultations(caregiverId);
    }

    @Test
    public void testCreateScheduleWithDateTimeAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        when(context.createScheduleWithDateTime(caregiverId, startTime, endTime)).thenReturn(true);

        // Execute
        CompletableFuture<Boolean> future = asyncService.createScheduleWithDateTimeAsync(caregiverId, startTime, endTime);
        Boolean result = future.get(); // Block to get the result

        // Verify
        assertTrue(result);
        verify(context).createScheduleWithDateTime(caregiverId, startTime, endTime);
    }

    @Test
    public void testModifyScheduleWithDateTimeAsync() throws ExecutionException, InterruptedException {
        // Setup
        String caregiverId = "C001";
        LocalDateTime oldStartTime = LocalDateTime.now().plusHours(1);
        LocalDateTime oldEndTime = LocalDateTime.now().plusHours(2);
        LocalDateTime newStartTime = LocalDateTime.now().plusHours(3);
        LocalDateTime newEndTime = LocalDateTime.now().plusHours(4);
        
        when(context.modifyScheduleWithDateTime(caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime)).thenReturn(true);

        // Execute
        CompletableFuture<Boolean> future = asyncService.modifyScheduleWithDateTimeAsync(
            caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        Boolean result = future.get(); // Block to get the result

        // Verify
        assertTrue(result);
        verify(context).modifyScheduleWithDateTime(caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
    }
}