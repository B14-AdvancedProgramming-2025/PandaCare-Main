package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncSchedulingService {
    
    private static final Logger log = LoggerFactory.getLogger(AsyncSchedulingService.class);
    private final SchedulingContext context;
    
    public AsyncSchedulingService(SchedulingContext context) {
        this.context = context;
    }
    
    @Async("schedulingTaskExecutor")
    public CompletableFuture<List<Consultation>> getCaregiverConsultationsAsync(String caregiverId) {
        log.info("Asynchronously fetching consultations for caregiver: {}", caregiverId);
        List<Consultation> consultations = context.getCaregiverConsultations(caregiverId);
        return CompletableFuture.completedFuture(consultations);
    }
    
    @Async("schedulingTaskExecutor")
    public CompletableFuture<List<Consultation>> getPatientConsultationsAsync(String pacilianId) {
        log.info("Asynchronously fetching consultations for patient: {}", pacilianId);
        List<Consultation> consultations = context.getPatientConsultations(pacilianId);
        return CompletableFuture.completedFuture(consultations);
    }
    
    @Async("schedulingTaskExecutor")
    public CompletableFuture<Boolean> createScheduleWithDateTimeAsync(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Asynchronously creating schedule for caregiver {}: {} to {}", caregiverId, startTime, endTime);
        boolean result = context.createScheduleWithDateTime(caregiverId, startTime, endTime);
        return CompletableFuture.completedFuture(result);
    }
    
    @Async("schedulingTaskExecutor")
    public CompletableFuture<Boolean> modifyScheduleWithDateTimeAsync(String caregiverId, LocalDateTime oldStartTime, 
                                                        LocalDateTime oldEndTime, 
                                                        LocalDateTime newStartTime, 
                                                        LocalDateTime newEndTime) {
        log.info("Asynchronously modifying schedule for caregiver {}: {} to {} -> {} to {}", 
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        boolean result = context.modifyScheduleWithDateTime(caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        return CompletableFuture.completedFuture(result);
    }
    
    @Async("schedulingTaskExecutor")
    public CompletableFuture<Boolean> bookConsultationWithDateTimeAsync(String caregiverId, String pacilianId, 
                                                        LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Asynchronously booking consultation for patient {} with caregiver {}: {} to {}", 
                pacilianId, caregiverId, startTime, endTime);
        boolean result = context.bookConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
        return CompletableFuture.completedFuture(result);
    }
    
    @Async("schedulingTaskExecutor")
    public CompletableFuture<List<Map<String, Object>>> findAvailableCaregiversAsync(LocalDateTime startTime, LocalDateTime endTime, String specialty) {
        log.info("Asynchronously finding available caregivers with specialty {} between {} and {}", specialty, startTime, endTime);
        List<Map<String, Object>> caregivers = context.findAvailableCaregivers(startTime, endTime, specialty);
        return CompletableFuture.completedFuture(caregivers);
    }

    @Async("schedulingTaskExecutor")
    public CompletableFuture<List<Map<String, Object>>> getCaregiverSchedulesFormattedAsync(String caregiverId) {
        log.info("Asynchronously fetching formatted schedules for caregiver: {}", caregiverId);
        List<Map<String, Object>> formattedSchedules = context.getCaregiverSchedulesFormatted(caregiverId);
        return CompletableFuture.completedFuture(formattedSchedules);
    }

    @Async("schedulingTaskExecutor")
    public CompletableFuture<Boolean> deleteScheduleWithDateTimeAsync(String caregiverId, 
                                                    LocalDateTime startTime, 
                                                    LocalDateTime endTime) {
        log.info("Asynchronously deleting schedule for caregiver {}: {} to {}", 
                caregiverId, startTime, endTime);
        boolean result = context.deleteScheduleWithDateTime(caregiverId, startTime, endTime);
        return CompletableFuture.completedFuture(result);
    }

    @Async("schedulingTaskExecutor")
    public CompletableFuture<Boolean> acceptConsultationWithDateTimeAsync(String caregiverId, 
                                                        String pacilianId, 
                                                        LocalDateTime startTime, 
                                                        LocalDateTime endTime) {
        log.info("Asynchronously accepting consultation for patient {} with caregiver {}: {} to {}", 
                pacilianId, caregiverId, startTime, endTime);
        boolean result = context.updateConsultationStatusWithDateTime(
            caregiverId, pacilianId, startTime, endTime, "ACCEPTED");
        return CompletableFuture.completedFuture(result);
    }

    @Async("schedulingTaskExecutor")
    public CompletableFuture<Boolean> rejectConsultationWithDateTimeAsync(String caregiverId, 
                                                        String pacilianId, 
                                                        LocalDateTime startTime, 
                                                        LocalDateTime endTime) {
        log.info("Asynchronously rejecting consultation for patient {} with caregiver {}: {} to {}", 
                pacilianId, caregiverId, startTime, endTime);
        boolean result = context.updateConsultationStatusWithDateTime(
            caregiverId, pacilianId, startTime, endTime, "REJECTED");
        return CompletableFuture.completedFuture(result);
    }
}