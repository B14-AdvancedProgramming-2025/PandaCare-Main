package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.AsyncSchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/scheduling/async")
public class AsyncSchedulingController {

    private static final Logger log = LoggerFactory.getLogger(AsyncSchedulingController.class);
    private final AsyncSchedulingService asyncSchedulingService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public AsyncSchedulingController(AsyncSchedulingService asyncSchedulingService) {
        this.asyncSchedulingService = asyncSchedulingService;
    }
    
    @GetMapping("/consultations/caregiver/{caregiverId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getCaregiverConsultationsAsync(@PathVariable String caregiverId) {
        log.info("Getting consultations asynchronously for caregiver: {}", caregiverId);
        
        return asyncSchedulingService.getCaregiverConsultationsAsync(caregiverId)
                .thenApply(consultations -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("consultations", consultations);
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    log.error("Error getting caregiver consultations", ex);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to get consultations: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }
    
    @GetMapping("/consultations/patient/{pacilianId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getPatientConsultationsAsync(@PathVariable String pacilianId) {
        log.info("Getting consultations asynchronously for patient: {}", pacilianId);
        
        return asyncSchedulingService.getPatientConsultationsAsync(pacilianId)
                .thenApply(consultations -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("consultations", consultations);
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    log.error("Error getting patient consultations", ex);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to get consultations: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }
    
    @PostMapping("/schedule/{caregiverId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createScheduleAsync(
            @PathVariable String caregiverId,
            @RequestBody Map<String, String> requestBody) {
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
            LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
            
            log.info("Creating schedule asynchronously for caregiver {}: {} to {}", caregiverId, startTime, endTime);
            
            return asyncSchedulingService.createScheduleWithDateTimeAsync(caregiverId, startTime, endTime)
                    .thenApply(result -> {
                        Map<String, Object> response = new HashMap<>();
                        if (result) {
                            response.put("success", true);
                            response.put("message", "Schedule created successfully");
                            return ResponseEntity.ok(response);
                        } else {
                            response.put("success", false);
                            response.put("message", "Failed to create schedule");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    })
                    .exceptionally(ex -> {
                        log.error("Error creating schedule", ex);
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "Failed to create schedule: " + ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    });
                
        } catch (Exception e) {
            log.error("Error parsing request", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request format: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
        }
    }
    
    @PutMapping("/schedule/{caregiverId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> modifyScheduleAsync(
            @PathVariable String caregiverId,
            @RequestBody Map<String, String> requestBody) {
        
        try {
            LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
            LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
            LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
            LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);
            
            log.info("Modifying schedule asynchronously for caregiver {}: {} to {} -> {} to {}", 
                    caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
            
            return asyncSchedulingService.modifyScheduleWithDateTimeAsync(
                    caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime)
                    .thenApply(result -> {
                        Map<String, Object> response = new HashMap<>();
                        if (result) {
                            response.put("success", true);
                            response.put("message", "Schedule modified successfully");
                            return ResponseEntity.ok(response);
                        } else {
                            response.put("success", false);
                            response.put("message", "Failed to modify schedule");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    })
                    .exceptionally(ex -> {
                        log.error("Error modifying schedule", ex);
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "Failed to modify schedule: " + ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    });
                
        } catch (Exception e) {
            log.error("Error parsing request", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request format: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
        }
    }
    
    @GetMapping("/available-caregivers")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> findAvailableCaregiversAsync(
            @RequestParam("startTime") String startTimeStr,
            @RequestParam("endTime") String endTimeStr,
            @RequestParam(value = "specialty", required = false) String specialty) {
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
            
            log.info("Finding available caregivers asynchronously with specialty {} between {} and {}", 
                    specialty, startTime, endTime);
            
            return asyncSchedulingService.findAvailableCaregiversAsync(startTime, endTime, specialty)
                    .thenApply(caregivers -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("caregivers", caregivers);
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(ex -> {
                        log.error("Error finding available caregivers", ex);
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "Failed to find available caregivers: " + ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    });
                
        } catch (Exception e) {
            log.error("Error parsing request", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request format: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
        }
    }
}