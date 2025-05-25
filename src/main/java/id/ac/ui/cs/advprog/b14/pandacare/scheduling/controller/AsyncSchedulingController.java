package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.AsyncSchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;

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
    private final UserRepository userRepository;
    
    public AsyncSchedulingController(AsyncSchedulingService asyncSchedulingService, UserRepository userRepository) {
        this.asyncSchedulingService = asyncSchedulingService;
        this.userRepository = userRepository;
    }
    
    @GetMapping("/consultations/caregiver/{caregiverId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getCaregiverConsultationsAsync(
            @PathVariable String caregiverId,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.CAREGIVER) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only caregivers can access caregiver consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        // Verify caregiver is accessing their own consultations
        if (!user.getId().equals(caregiverId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You can only view your own consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
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
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getPatientConsultationsAsync(
            @PathVariable String pacilianId,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.PACILIAN) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only patients can access patient consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        // Verify patient is accessing their own consultations
        if (!user.getId().equals(pacilianId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You can only view your own consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
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

    @PostMapping("/consultations")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> bookConsultationAsync(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.PACILIAN) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only patients can book consultations");
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
                    Map<String, Object> response = new HashMap<>();
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
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to book consultation: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @PutMapping("/consultations/{caregiverId}/{pacilianId}/accept")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> acceptConsultationAsync(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.CAREGIVER) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only caregivers can accept consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        // Verify the caregiver is accepting their own consultation
        if (!user.getId().equals(caregiverId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You can only accept your own consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        log.info("Accepting consultation asynchronously: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startDateTime, endDateTime);
        
        return asyncSchedulingService.acceptConsultationWithDateTimeAsync(caregiverId, pacilianId, startDateTime, endDateTime)
                .thenApply(result -> {
                    Map<String, Object> response = new HashMap<>();
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
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to accept consultation: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @PutMapping("/consultations/{caregiverId}/{pacilianId}/reject")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> rejectConsultationAsync(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.CAREGIVER) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only caregivers can reject consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        // Verify the caregiver is rejecting their own consultation
        if (!user.getId().equals(caregiverId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You can only reject your own consultations");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        log.info("Rejecting consultation asynchronously: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startDateTime, endDateTime);
        
        return asyncSchedulingService.rejectConsultationWithDateTimeAsync(caregiverId, pacilianId, startDateTime, endDateTime)
                .thenApply(result -> {
                    Map<String, Object> response = new HashMap<>();
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
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to reject consultation: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    
    @PostMapping("/schedule")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createScheduleAsync(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.CAREGIVER) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only caregivers can create schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        String caregiverId = user.getId();
        
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
    
    @PutMapping("/schedule")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> modifyScheduleAsync(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.CAREGIVER) {
            Map<String, Object> response = new HashMap<>();
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

    @DeleteMapping("/schedule/{caregiverId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> deleteScheduleAsync(
            @PathVariable String caregiverId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null || user.getType() != UserType.CAREGIVER) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Only caregivers can delete schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        // Verify caregiver is deleting their own schedule
        if (!user.getId().equals(caregiverId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You can only delete your own schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        log.info("Deleting schedule asynchronously: caregiver={}, startTime={}, endTime={}",
                caregiverId, startDateTime, endDateTime);
        
        return asyncSchedulingService.deleteScheduleWithDateTimeAsync(caregiverId, startDateTime, endDateTime)
                .thenApply(result -> {
                    Map<String, Object> response = new HashMap<>();
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
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to delete schedule: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }

    @GetMapping("/schedules/{caregiverId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getCaregiverSchedulesAsync(
            @PathVariable String caregiverId,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Authentication required");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
        // If user is a caregiver, they can only view their own schedule
        if (user.getType() == UserType.CAREGIVER && !user.getId().equals(caregiverId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You can only view your own schedules");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(response));
        }
        
        log.info("Getting schedules asynchronously for caregiver {}", caregiverId);
        
        return asyncSchedulingService.getCaregiverSchedulesFormattedAsync(caregiverId)
                .thenApply(formattedSchedules -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("schedules", formattedSchedules);
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> {
                    log.error("Error getting caregiver schedules", ex);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "Failed to get schedules: " + ex.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                });
    }
    
    @GetMapping("/available-caregivers")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> findAvailableCaregiversAsync(
            @RequestParam("startTime") String startTimeStr,
            @RequestParam("endTime") String endTimeStr,
            @RequestParam(value = "specialty", required = false) String specialty,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Authentication required");
            return CompletableFuture.completedFuture(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
        
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