package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private static final Logger log = LoggerFactory.getLogger(SchedulingController.class);
    private final SchedulingService schedulingService;
    
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }
    
    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> createSchedule(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String schedule = requestBody.get("schedule");
        Map<String, Object> response = new HashMap<>();
        
        log.info("Creating schedule for caregiver {}: {}", caregiverId, schedule);
        
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

    @GetMapping("/schedules/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getCaregiverSchedules(@PathVariable String caregiverId) {
        log.info("Getting schedules for caregiver {}", caregiverId);
        
        List<String> schedules = schedulingService.getCaregiverSchedules(caregiverId);
        Map<String, Object> response = new HashMap<>();
        
        response.put("success", true);
        response.put("schedules", schedules);
        
        return ResponseEntity.ok(response);
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

    @DeleteMapping("/schedule/{caregiverId}/{schedule}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(
            @PathVariable String caregiverId, 
            @PathVariable String schedule) {
        
        Map<String, Object> response = new HashMap<>();
        
        log.info("Deleting schedule for caregiver {}: {}", caregiverId, schedule);
        
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
}