package id.ac.ui.cs.advprog.b14.pandacare.scheduling.controller;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private final SchedulingService schedulingService;
    
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }
    
    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> createSchedule(@RequestBody Map<String, String> requestBody) {
        return null;
    }
    
    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookConsultation(@RequestBody Map<String, String> requestBody) {
        return null;
    }
    
    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> acceptConsultation(@RequestBody Map<String, String> requestBody) {
        return null;
    }
    
    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> rejectConsultation(@RequestBody Map<String, String> requestBody) {
        return null;
    }
    
    @PostMapping("/modify")
    public ResponseEntity<Map<String, Object>> modifyConsultation(@RequestBody Map<String, String> requestBody) {
        return null;
    }
}