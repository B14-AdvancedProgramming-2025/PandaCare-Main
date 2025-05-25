package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;

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
    private final UserRepository userRepository;

    public SchedulingController(SchedulingService schedulingService, UserRepository userRepository) {
        this.schedulingService = schedulingService;
        this.userRepository = userRepository;
    }

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> createSchedule(@
            RequestBody Map<String, String> requestBody,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can create schedules");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String caregiverId = user.getId();
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
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
    public ResponseEntity<Map<String, Object>> bookConsultation(
        @RequestBody Map<String, String> requestBody,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.PACILIAN) {
            response.put("success", false);
            response.put("message", "Only patients can book consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String pacilianId = user.getId();
        String caregiverId = requestBody.get("caregiverId");
        LocalDateTime startTime = LocalDateTime.parse(requestBody.get("startTime"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestBody.get("endTime"), formatter);
        
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
            @RequestParam String endTime,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can accept consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        // Verify the caregiver is accepting their own consultation
        if (!user.getId().equals(caregiverId)) {
            response.put("success", false);
            response.put("message", "You can only accept your own consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
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
            @RequestParam String endTime,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can reject consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        // Verify the caregiver is rejecting their own consultation
        if (!user.getId().equals(caregiverId)) {
            response.put("success", false);
            response.put("message", "You can only reject your own consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
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
    public ResponseEntity<Map<String, Object>> getCaregiverSchedules(
        @PathVariable String caregiverId,
        Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        // Allow both caregiver (to see their own) and patients (to see any caregiver's schedule)
        if (user == null) {
            response.put("success", false);
            response.put("message", "Authentication required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        // If user is a caregiver, they can only view their own schedule
        if (user.getType() == UserType.CAREGIVER && !user.getId().equals(caregiverId)) {
            response.put("success", false);
            response.put("message", "You can only view your own schedules");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        log.info("Getting schedules for caregiver {}", caregiverId);
        
        try {
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
    public ResponseEntity<Map<String, Object>> getCaregiverConsultations(
        @PathVariable String caregiverId,
        Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can access caregiver consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        // Verify caregiver is accessing their own consultations
        if (!user.getId().equals(caregiverId)) {
            response.put("success", false);
            response.put("message", "You can only view your own consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        log.info("Getting consultations for caregiver {}", caregiverId);
        
        List<Consultation> consultations = schedulingService.getCaregiverConsultations(caregiverId);
        
        response.put("success", true);
        response.put("consultations", consultations);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/consultations/patient/{pacilianId}")
    public ResponseEntity<Map<String, Object>> getPatientConsultations(
        @PathVariable String pacilianId,
        Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.PACILIAN) {
            response.put("success", false);
            response.put("message", "Only patients can access patient consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        // Verify patient is accessing their own consultations
        if (!user.getId().equals(pacilianId)) {
            response.put("success", false);
            response.put("message", "You can only view your own consultations");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        log.info("Getting consultations for patient {}", pacilianId);
        
        List<Consultation> consultations = schedulingService.getPatientConsultations(pacilianId);
        
        response.put("success", true);
        response.put("consultations", consultations);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/schedule/{caregiverId}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(
            @PathVariable String caregiverId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            Authentication authentication) {
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can delete schedules");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        // Verify caregiver is deleting their own schedule
        if (!user.getId().equals(caregiverId)) {
            response.put("success", false);
            response.put("message", "You can only delete your own schedules");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
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
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null || user.getType() != UserType.CAREGIVER) {
            response.put("success", false);
            response.put("message", "Only caregivers can modify schedules");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        
        String caregiverId = user.getId();
        
        // Verify caregiverId in request matches authenticated user
        if (!caregiverId.equals(requestBody.get("caregiverId"))) {
            response.put("success", false);
            response.put("message", "You can only modify your own schedules");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        LocalDateTime oldStartTime = LocalDateTime.parse(requestBody.get("oldStartTime"), formatter);
        LocalDateTime oldEndTime = LocalDateTime.parse(requestBody.get("oldEndTime"), formatter);
        LocalDateTime newStartTime = LocalDateTime.parse(requestBody.get("newStartTime"), formatter);
        LocalDateTime newEndTime = LocalDateTime.parse(requestBody.get("newEndTime"), formatter);
        
        
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
            @RequestParam(required = false) String specialty,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user == null) {
            response.put("success", false);
            response.put("message", "Authentication required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        log.info("Finding available caregivers for startTime={}, endTime={}, specialty={}", 
                startTime, endTime, specialty);
        
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
            
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