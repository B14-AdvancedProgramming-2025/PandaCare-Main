package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private static final Logger log = LoggerFactory.getLogger(SchedulingController.class);
    private final SchedulingService asyncSchedulingService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public SchedulingController(SchedulingService asyncSchedulingService) { 
        this.asyncSchedulingService = asyncSchedulingService;
    }
    
    @GetMapping("/caregiver/consultations")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getCaregiverConsultationsAsync(
            @AuthenticationPrincipal User user) { 
        log.info("Entering getCaregiverConsultationsAsync. Authenticated user: {}", (user != null ? user.getEmail() : "null"));
        
        Map<String, Object> responseMap = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for getCaregiverConsultationsAsync.");
            responseMap.put("success", false);
            responseMap.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap));
        }
        
        log.info("User found via @AuthenticationPrincipal: ID={}, Email={}, Type={}", user.getId(), user.getEmail(), user.getType());

        if (user.getType() != UserType.CAREGIVER) {
            log.warn("Access denied for getCaregiverConsultationsAsync. User ID: {}, User Type: {}. Expected CAREGIVER.", user.getId(), user.getType());
            responseMap.put("success", false);
            responseMap.put("message", "Only caregivers can access caregiver consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap));
        }
        
        String caregiverId = user.getId(); 
        log.info("Getting consultations asynchronously for caregiver: {}", caregiverId);
        
        return asyncSchedulingService.getCaregiverConsultationsAsync(caregiverId)
                .thenApply(consultations -> {
                    responseMap.put("success", true);
                    responseMap.put("consultations", consultations);
                    return ResponseEntity.ok(responseMap);
                })
                .exceptionally(ex -> {
                    log.error("Error getting caregiver consultations for caregiverId {}: {}", caregiverId, ex.getMessage(), ex);
                    responseMap.put("success", false);
                    responseMap.put("message", "Failed to get consultations: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
                });
    }
    
    @GetMapping("/pacilian/consultations")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getPacilianConsultationsAsync(
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for getPacilianConsultationsAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.PACILIAN) {
            response.put("success", false);
            response.put("message", "Only pacilians can access pacilian consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String pacilianId = user.getId();
        
        log.info("Getting consultations asynchronously for pacilian: {}", pacilianId);
        
        return asyncSchedulingService.getPacilianConsultationsAsync(pacilianId)
                .thenApply(consultations -> {
                    response.put("success", true);
                    response.put("consultations", consultations);
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    log.error("Error getting pacilian consultations", ex);
                    response.put("success", false);
                    response.put("message", "Failed to get consultations: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @PostMapping("/pacilian/consultations")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> bookConsultationAsync(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>();

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for bookConsultationAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.PACILIAN) {
            response.put("success", false);
            response.put("message", "Only pacilians can book consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String pacilianId = user.getId();
        String caregiverId = requestBody.get("caregiverId");
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
        log.info("Booking consultation asynchronously: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startTime, endTime);
        
        return asyncSchedulingService.bookConsultationWithDateTimeAsync(caregiverId, pacilianId, startTime, endTime)
                .thenApply(result -> {
                    if (result) {
                        response.put("success", true);
                        response.put("message", "Consultation booked successfully");
                        return ResponseEntity.status(HttpStatus.CREATED).body(response);
                    } else {
                        response.put("success", false);
                        response.put("message", "Failed to book consultation");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                })
                .exceptionally(ex -> {
                    log.error("Error booking consultation", ex);
                    response.put("success", false);
                    response.put("message", "Failed to book consultation: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @PutMapping("/caregiver/consultations/accept")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> acceptConsultationAsync(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for acceptConsultationAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can accept consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String caregiverId = user.getId();
        String pacilianId = requestBody.get("pacilianId");
        String startTime = requestBody.get("startTime");
        String endTime = requestBody.get("endTime");
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        log.info("Accepting consultation asynchronously: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startDateTime, endDateTime);
        
        return asyncSchedulingService.acceptConsultationWithDateTimeAsync(caregiverId, pacilianId, startDateTime, endDateTime)
                .thenApply(result -> {
                    if (result) {
                        response.put("success", true);
                        response.put("message", "Consultation accepted successfully");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("success", false);
                        response.put("message", "Failed to accept consultation");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                })
                .exceptionally(ex -> {
                    log.error("Error accepting consultation", ex);
                    response.put("success", false);
                    response.put("message", "Failed to accept consultation: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @PutMapping("/caregiver/consultations/reject")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> rejectConsultationAsync(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for rejectConsultationAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can reject consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String caregiverId = user.getId();
        String pacilianId = requestBody.get("pacilianId");
        String startTime = requestBody.get("startTime");
        String endTime = requestBody.get("endTime");
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        log.info("Rejecting consultation asynchronously: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startDateTime, endDateTime);
        
        return asyncSchedulingService.rejectConsultationWithDateTimeAsync(caregiverId, pacilianId, startDateTime, endDateTime)
                .thenApply(result -> {
                    if (result) {
                        response.put("success", true);
                        response.put("message", "Consultation rejected successfully");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("success", false);
                        response.put("message", "Failed to reject consultation");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                })
                .exceptionally(ex -> {
                    log.error("Error rejecting consultation", ex);
                    response.put("success", false);
                    response.put("message", "Failed to reject consultation: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    
    @PostMapping("/caregiver/schedules")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createScheduleAsync(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User user) { 
        log.info("Entering createScheduleAsync. Authenticated user: {}", (user != null ? user.getEmail() : "null"));
        
        Map<String, Object> responseMap = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for createScheduleAsync.");
            responseMap.put("success", false);
            responseMap.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap));
        }

        log.info("User found via @AuthenticationPrincipal: ID={}, Email={}, Type={}", user.getId(), user.getEmail(), user.getType());
        
        if (user.getType() != UserType.CAREGIVER) {
            log.warn("Access denied for createScheduleAsync. User ID: {}, User Type: {}. Expected CAREGIVER.", user.getId(), user.getType());
            responseMap.put("success", false);
            responseMap.put("message", "Only caregivers can create schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap));
        }
        
        String caregiverId = user.getId();
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
            LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
            
            log.info("Creating schedule asynchronously for caregiver {}: {} to {}", caregiverId, startTime, endTime);
            
            return asyncSchedulingService.createScheduleWithDateTimeAsync(caregiverId, startTime, endTime)
                    .thenApply(result -> {
                        if (result) {
                            responseMap.put("success", true);
                            responseMap.put("message", "Schedule created successfully");
                            return ResponseEntity.ok(responseMap);
                        } else {
                            responseMap.put("success", false);
                            responseMap.put("message", "Failed to create schedule");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
                        }
                    })
                    .exceptionally(ex -> {
                        log.error("Error creating schedule for caregiverId {}: {}", caregiverId, ex.getMessage(), ex);
                        responseMap.put("success", false);
                        responseMap.put("message", "Failed to create schedule: " + ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
                    });
                
        } catch (Exception e) {
            log.error("Error parsing request for create schedule for caregiverId {}: {}", caregiverId, e.getMessage(), e);
            responseMap.put("success", false);
            responseMap.put("message", "Invalid request format: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(responseMap));
        }
    }
    
    @PutMapping("/caregiver/schedules")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> modifyScheduleAsync(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for modifyScheduleAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can modify schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String caregiverId = user.getId();
        
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
                        response.put("success", false);
                        response.put("message", "Failed to modify schedule: " + ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    });
                
        } catch (Exception e) {
            log.error("Error parsing request", e);
            response.put("success", false);
            response.put("message", "Invalid request format: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
        }
    }

    @DeleteMapping("/caregiver/schedules")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteScheduleAsync(
            @RequestBody Map<String, String> requestBody,
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for deleteScheduleAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can delete schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String caregiverId = user.getId();
        String startTime = requestBody.get("startTime");
        String endTime = requestBody.get("endTime");
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        log.info("Deleting schedule asynchronously: caregiver={}, startTime={}, endTime={}",
                caregiverId, startDateTime, endDateTime);
        
        return asyncSchedulingService.deleteScheduleWithDateTimeAsync(caregiverId, startDateTime, endDateTime)
                .thenApply(result -> {
                    if (result) {
                        response.put("success", true);
                        response.put("message", "Schedule deleted successfully");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("success", false);
                        response.put("message", "Failed to delete schedule");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                })
                .exceptionally(ex -> {
                    log.error("Error deleting schedule", ex);
                    response.put("success", false);
                    response.put("message", "Failed to delete schedule: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @GetMapping("/caregiver/schedules")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getCaregiverSchedulesAsync(
            @AuthenticationPrincipal User user) { 
        log.info("Entering getCaregiverSchedulesAsync. Authenticated user: {}", (user != null ? user.getEmail() : "null"));
        
        Map<String, Object> responseMap = new HashMap<>();

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for getCaregiverSchedulesAsync.");
            responseMap.put("success", false);
            responseMap.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap));
        }

        log.info("User found via @AuthenticationPrincipal: ID={}, Email={}, Type={}", user.getId(), user.getEmail(), user.getType());
        
        if (user.getType() != UserType.CAREGIVER) {
            log.warn("Access denied for getCaregiverSchedulesAsync. User ID: {}, User Type: {}. Expected CAREGIVER.", user.getId(), user.getType());
            responseMap.put("success", false);
            responseMap.put("message", "Only caregivers can view their schedules this way.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap));
        }
        String caregiverId = user.getId(); 
        
        log.info("Getting schedules asynchronously for caregiver {}", caregiverId);
        
        return asyncSchedulingService.getCaregiverSchedulesFormattedAsync(caregiverId)
                .thenApply(formattedSchedules -> {
                    log.info("Successfully retrieved {} schedules for caregiver {}", 
                    formattedSchedules.size(), caregiverId);
                    responseMap.put("success", true);
                    responseMap.put("schedules", formattedSchedules);
                    ResponseEntity<Map<String, Object>> response = ResponseEntity.ok(responseMap);
                    log.info("Returning response with status: {}", response.getStatusCode());
                    return ResponseEntity.ok(responseMap);
                })
                .exceptionally(ex -> {
                    log.error("Error getting caregiver schedules for caregiverId {}: {}", caregiverId, ex.getMessage(), ex);
                    responseMap.put("success", false);
                    responseMap.put("message", "Failed to get schedules: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
                });
    }
    
    @GetMapping("/pacilian/available-caregivers")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> findAvailableCaregiversAsync(
            @RequestParam("startTime") String startTimeStr,
            @RequestParam("endTime") String endTimeStr,
            @RequestParam(value = "specialty", required = false) String specialty,
            @AuthenticationPrincipal User user) { 
        
        Map<String, Object> response = new HashMap<>(); 

        if (user == null) {
            log.warn("User from @AuthenticationPrincipal is null for findAvailableCaregiversAsync.");
            response.put("success", false);
            response.put("message", "User not authenticated or not found.");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        if (user.getType() != UserType.PACILIAN) {
            response.put("success", false);
            response.put("message", "Only pacilians can find available caregivers");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
            
            log.info("Finding available caregivers asynchronously with specialty {} between {} and {}", 
                    specialty, startTime, endTime);
            
            return asyncSchedulingService.findAvailableCaregiversAsync(startTime, endTime, specialty)
                    .thenApply(caregivers -> {
                        response.put("success", true);
                        response.put("caregivers", caregivers);
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(ex -> {
                        log.error("Error finding available caregivers", ex);
                        response.put("success", false);
                        response.put("message", "Failed to find available caregivers: " + ex.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    });
                
        } catch (Exception e) {
            log.error("Error parsing request", e);
            response.put("success", false);
            response.put("message", "Invalid request format: " + e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(response));
        }
    }
}