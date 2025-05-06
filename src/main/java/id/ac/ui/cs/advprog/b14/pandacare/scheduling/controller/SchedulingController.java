package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to create schedule");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/book")
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
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Failed to book consultation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> acceptConsultation(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String pacilianId = requestBody.get("pacilianId");
        String schedule = requestBody.get("schedule");
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
    
    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> rejectConsultation(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String pacilianId = requestBody.get("pacilianId");
        String schedule = requestBody.get("schedule");
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
    
    @PostMapping("/modify")
    public ResponseEntity<Map<String, Object>> modifyConsultation(@RequestBody Map<String, String> requestBody) {
        String caregiverId = requestBody.get("caregiverId");
        String pacilianId = requestBody.get("pacilianId");
        String schedule = requestBody.get("schedule");
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
}