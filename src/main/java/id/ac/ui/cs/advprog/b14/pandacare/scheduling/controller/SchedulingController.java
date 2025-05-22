package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private static final Logger log = LoggerFactory.getLogger(SchedulingController.class);
    private final SchedulingService schedulingService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }
    
    // Legacy endpoints for backward compatibility
    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> createSchedule(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String schedule = requestBody.get("schedule");
        Map<String, Object> response = new HashMap<>();
        
        log.info("Creating schedule: caregiver={}, schedule={}", caregiverId, schedule);
        
        boolean result = schedulingService.createSchedule(caregiverId, schedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Schedule created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to create schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // New endpoint with DateTime
    @PostMapping("/schedule/datetime")
    public ResponseEntity<Map<String, Object>> createScheduleWithDateTime(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Creating schedule with date time: caregiver={}, startTime={}, endTime={}", 
                caregiverId, startTime, endTime);
        
        boolean result = schedulingService.createScheduleWithDateTime(caregiverId, startTime, endTime);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Schedule created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to create schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Legacy endpoint for booking consultation
    @PostMapping("/consultations")
    public ResponseEntity<Map<String, Object>> bookConsultation(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String pacilianId = requestBody.get("pacilianId");
        String schedule = requestBody.get("schedule");
        Map<String, Object> response = new HashMap<>();
        
        log.info("Booking consultation: caregiver={}, pacilian={}, schedule={}",
                caregiverId, pacilianId, schedule);
        
        boolean result = schedulingService.bookConsultation(caregiverId, pacilianId, schedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation booked successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to book consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // New endpoint for booking consultation with DateTime
    @PostMapping("/consultations/datetime")
    public ResponseEntity<Map<String, Object>> bookConsultationWithDateTime(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String pacilianId = requestBody.get("pacilianId");
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Booking consultation with date time: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startTime, endTime);
        
        boolean result = schedulingService.bookConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation booked successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to book consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Legacy endpoint for accepting consultation
    @PutMapping("/consultations/{caregiverId}/{pacilianId}/{schedule}/accept")
    public ResponseEntity<Map<String, Object>> acceptConsultation(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId,
            @PathVariable String schedule) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Accepting consultation: caregiver={}, pacilian={}, schedule={}",
                caregiverId, pacilianId, schedule);
        
        boolean result = schedulingService.acceptConsultation(caregiverId, pacilianId, schedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation accepted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to accept consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // New endpoint for accepting consultation with DateTime
    @PutMapping("/consultations/{caregiverId}/{pacilianId}/datetime/accept")
    public ResponseEntity<Map<String, Object>> acceptConsultationWithDateTime(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Accepting consultation with date time: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startTime, endTime);
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        boolean result = schedulingService.acceptConsultationWithDateTime(caregiverId, pacilianId, startDateTime, endDateTime);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation accepted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to accept consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Legacy endpoint for rejecting consultation
    @PutMapping("/consultations/{caregiverId}/{pacilianId}/{schedule}/reject")
    public ResponseEntity<Map<String, Object>> rejectConsultation(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId,
            @PathVariable String schedule) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Rejecting consultation: caregiver={}, pacilian={}, schedule={}",
                caregiverId, pacilianId, schedule);
        
        boolean result = schedulingService.rejectConsultation(caregiverId, pacilianId, schedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation rejected successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to reject consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // New endpoint for rejecting consultation with DateTime
    @PutMapping("/consultations/{caregiverId}/{pacilianId}/datetime/reject")
    public ResponseEntity<Map<String, Object>> rejectConsultationWithDateTime(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Rejecting consultation with date time: caregiver={}, pacilian={}, startTime={}, endTime={}",
                caregiverId, pacilianId, startTime, endTime);
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        boolean result = schedulingService.rejectConsultationWithDateTime(caregiverId, pacilianId, startDateTime, endDateTime);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation rejected successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to reject consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Legacy endpoint for modifying consultation
    @PutMapping("/consultations/{caregiverId}/{pacilianId}/{schedule}")
    public ResponseEntity<Map<String, Object>> modifyConsultation(
            @PathVariable String caregiverId,
            @PathVariable String pacilianId, 
            @PathVariable String schedule,
            @RequestBody Map<String, String> requestBody) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Modifying consultation: caregiver={}, pacilian={}, schedule={}",
                caregiverId, pacilianId, schedule);
        
        boolean result = schedulingService.modifyConsultation(caregiverId, pacilianId, schedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Consultation modified successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to modify consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Legacy endpoint for getting schedules
    @GetMapping("/schedules/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getCaregiverSchedules(@PathVariable String caregiverId) {
        log.info("Getting schedules for caregiver {}", caregiverId);
        
        List<String> schedules = schedulingService.getCaregiverSchedules(caregiverId);
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("schedules", schedules);
        
        return ResponseEntity.ok(response);
    }
    
    // Legacy endpoint for getting consultations by caregiver
    @GetMapping("/consultations/caregiver/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getCaregiverConsultations(@PathVariable String caregiverId) {
        log.info("Getting consultations for caregiver {}", caregiverId);
        
        List<Consultation> consultations = schedulingService.getCaregiverConsultations(caregiverId);
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("consultations", consultations);
        
        return ResponseEntity.ok(response);
    }
    
    // Legacy endpoint for getting consultations by patient
    @GetMapping("/consultations/patient/{pacilianId}")
    public ResponseEntity<Map<String, Object>> getPatientConsultations(@PathVariable String pacilianId) {
        log.info("Getting consultations for patient {}", pacilianId);
        
        List<Consultation> consultations = schedulingService.getPatientConsultations(pacilianId);
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("consultations", consultations);
        
        return ResponseEntity.ok(response);
    }
    
    // Legacy endpoint for deleting schedule
    @DeleteMapping("/schedule/{caregiverId}/{schedule}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(
            @PathVariable String caregiverId, 
            @PathVariable String schedule) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Deleting schedule: caregiver={}, schedule={}", caregiverId, schedule);
        
        boolean result = schedulingService.deleteSchedule(caregiverId, schedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Schedule deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to delete schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // New endpoint for deleting schedule with DateTime
    @DeleteMapping("/schedule/{caregiverId}/datetime")
    public ResponseEntity<Map<String, Object>> deleteScheduleWithDateTime(
            @PathVariable String caregiverId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Deleting schedule with date time: caregiver={}, startTime={}, endTime={}", 
                caregiverId, startTime, endTime);
        
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
        
        boolean result = schedulingService.deleteScheduleWithDateTime(caregiverId, startDateTime, endDateTime);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Schedule deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to delete schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Legacy endpoint for modifying schedule
    @PutMapping("/schedule/{caregiverId}/{oldSchedule}")
    public ResponseEntity<Map<String, Object>> modifySchedule(
            @PathVariable String caregiverId,
            @PathVariable String oldSchedule,
            @RequestBody Map<String, String> requestBody) {
        
        String newSchedule = requestBody.get("newSchedule");
        Map<String, Object> response = new HashMap<>();
        
        log.info("Modifying schedule for caregiver {}: {} -> {}", 
                caregiverId, oldSchedule, newSchedule);
        
        boolean result = schedulingService.modifySchedule(caregiverId, oldSchedule, newSchedule);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Schedule modified successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to modify schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // New endpoint for modifying schedule with DateTime
    @PutMapping("/schedule/{caregiverId}/datetime")
    public ResponseEntity<Map<String, Object>> modifyScheduleWithDateTime(
            @RequestBody Map<String, String> requestBody) {
        
        String caregiverId = requestBody.get("caregiverId");
        LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
        LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
        LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Modifying schedule with date time for caregiver {}: {} to {} -> {} to {}", 
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        boolean result = schedulingService.modifyScheduleWithDateTime(
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        if (result) {
            response.put("success", true);
            response.put("message", "Schedule modified successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to modify schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // Add method to the SchedulingService interface for deleteScheduleWithDateTime
    @DeleteMapping("/schedule/{caregiverId}/datetime")
    public boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        return schedulingService.deleteScheduleWithDateTime(caregiverId, startTime, endTime);
    }
}