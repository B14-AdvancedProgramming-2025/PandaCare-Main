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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> createSchedule(@RequestBody Map<String, String> requestBody) {
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

    @PostMapping("/consultations")
    public ResponseEntity<Map<String, Object>> bookConsultation(@RequestBody Map<String, String> requestBody) {
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
    
    @PutMapping("/consultations/{caregiverId}/{pacilianId}/accept")
    public ResponseEntity<Map<String, Object>> acceptConsultation(
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

    @PutMapping("/consultations/{caregiverId}/{pacilianId}/reject")
    public ResponseEntity<Map<String, Object>> rejectConsultation(
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
    
    
    @GetMapping("/schedules/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getCaregiverSchedules(@PathVariable String caregiverId) {
        log.info("Getting schedules for caregiver {}", caregiverId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Use our new service method to get formatted schedules directly
            List<Map<String, Object>> formattedSchedules = schedulingService.getCaregiverSchedulesFormatted(caregiverId);
            
            log.info("Found {} schedules for caregiver {}", formattedSchedules.size(), caregiverId);
            
            response.put("success", true);
            response.put("schedules", formattedSchedules);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting caregiver schedules", e);
            response.put("success", false);
            response.put("message", "Error retrieving schedules: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/consultations/caregiver/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getCaregiverConsultations(@PathVariable String caregiverId) {
        log.info("Getting consultations for caregiver {}", caregiverId);
        
        List<Consultation> consultations = schedulingService.getCaregiverConsultations(caregiverId);
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("consultations", consultations);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/consultations/patient/{pacilianId}")
    public ResponseEntity<Map<String, Object>> getPatientConsultations(@PathVariable String pacilianId) {
        log.info("Getting consultations for patient {}", pacilianId);
        
        List<Consultation> consultations = schedulingService.getPatientConsultations(pacilianId);
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("consultations", consultations);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/schedule/{caregiverId}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(
            @PathVariable String caregiverId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
            
            boolean result = schedulingService.deleteScheduleWithDateTime(
                    caregiverId, startDateTime, endDateTime);
            
            if (result) {
                response.put("success", true);
                response.put("message", "Schedule deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete schedule");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PutMapping("/schedule/{caregiverId}")
    public ResponseEntity<Map<String, Object>> modifySchedule(
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
    


    @GetMapping("/caregivers/available")
    public ResponseEntity<Map<String, Object>> findAvailableCaregivers(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false) String specialty) {
        
        log.info("Finding available caregivers for startTime={}, endTime={}, specialty={}", 
                startTime, endTime, specialty);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startTime);
            LocalDateTime endDateTime = LocalDateTime.parse(endTime);
            
            List<Map<String, Object>> availableCaregivers = 
                    schedulingService.findAvailableCaregivers(startDateTime, endDateTime, specialty);
            
            response.put("success", true);
            response.put("caregivers", availableCaregivers);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error finding available caregivers", e);
            response.put("success", false);
            response.put("message", "Error finding available caregivers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}