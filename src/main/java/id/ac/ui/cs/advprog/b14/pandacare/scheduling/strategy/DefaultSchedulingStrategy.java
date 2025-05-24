package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultSchedulingStrategy implements SchedulingStrategy {

    private static final Logger log = LoggerFactory.getLogger(DefaultSchedulingStrategy.class);
    private final ScheduleRepository scheduleRepository;
    private final ConsultationRepository consultationRepository;
    private final ConsultationService consultationService;
    private final CaregiverRepositoryAdapter caregiverAdapter;
    private final WorkingScheduleRepository workingScheduleRepository;
    
    public DefaultSchedulingStrategy(
            ScheduleRepository scheduleRepository, 
            ConsultationRepository consultationRepository,
            WorkingScheduleRepository workingScheduleRepository,
            ConsultationService consultationService,
            CaregiverRepositoryAdapter caregiverAdapter) {
        this.scheduleRepository = scheduleRepository;
        this.consultationRepository = consultationRepository;
        this.workingScheduleRepository = workingScheduleRepository;
        this.consultationService = consultationService;
        this.caregiverAdapter = caregiverAdapter;
    }

    @Override
    public List<Consultation> getCaregiverConsultations(String caregiverId) {
        log.info("Getting consultations for caregiver {}", caregiverId);
        return consultationService.findConsultationsByCaregiverId(caregiverId);
    }

    @Override
    public List<Consultation> getPatientConsultations(String pacilianId) {
        log.info("Getting consultations for patient {}", pacilianId);
        return consultationService.findConsultationsByPacilianId(pacilianId);
    }

    @Override
    public List<String> getCaregiverSchedules(String caregiverId) {
        return scheduleRepository.getCaregiverSchedules(caregiverId);
    }

    // New methods implementation
    @Override
    public boolean createScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Creating schedule for caregiver {}: {} to {}", 
                caregiverId, startTime, endTime);
        return scheduleRepository.saveScheduleWithDateTime(caregiverId, startTime, endTime);
    }
    
    @Override
    public boolean bookConsultationWithDateTime(String caregiverId, String pacilianId, 
                                            LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Booking consultation for caregiver {} with patient {}: {} to {}", 
                caregiverId, pacilianId, startTime, endTime);
        
        // Check if the schedule is available
        if (!scheduleRepository.isScheduleAvailableByDateTime(caregiverId, startTime, endTime)) {
            log.warn("Schedule is not available for booking: {} to {}", startTime, endTime);
            return false;
        }
        
        // Save the consultation
        return consultationService.saveConsultationWithDateTime(caregiverId, pacilianId, startTime, endTime, "PENDING");
    }
    
    @Override
    public boolean updateConsultationStatusWithDateTime(String caregiverId, String pacilianId, 
                                                    LocalDateTime startTime, LocalDateTime endTime, String status) {
        log.info("Updating consultation status for caregiver {} with patient {}: {} to {} -> {}", 
                caregiverId, pacilianId, startTime, endTime, status);
        return consultationService.updateStatusWithDateTime(caregiverId, pacilianId, startTime, endTime, status);
    }
    
    @Override
    public boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime) {
        // Implement this method to replace deleteSchedule
        log.info("Deleting schedule with datetime for caregiver {}: {} to {}", caregiverId, startTime, endTime);
        
        // Find all consultations for this schedule
        List<Consultation> consultations = consultationRepository.findByCaregiverIdAndStartTimeBetween(
                caregiverId, startTime, endTime);
        
        for (Consultation consultation : consultations) {
            // Only allowed to delete if all consultations are in REJECTED status
            if (!"REJECTED".equals(consultation.getStatus())) {
                log.warn("Cannot delete schedule with non-rejected consultations: {} to {}", startTime, endTime);
                return false;
            }
        }

        for (Consultation consultation : consultations) {
            consultationRepository.delete(consultation);
            log.info("Deleted rejected consultation for schedule: {} to {}", startTime, endTime);
        }
        
        return scheduleRepository.deleteScheduleWithDateTime(caregiverId, startTime, endTime);
    }
    
    @Override
    public boolean modifyScheduleWithDateTime(String caregiverId, 
                                        LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                        LocalDateTime newStartTime, LocalDateTime newEndTime) {
        log.info("Modifying schedule for caregiver {}: {} to {} -> {} to {}", 
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        // Find all consultations for this schedule
        List<Consultation> consultations = consultationRepository.findByCaregiverIdAndStartTimeBetween(
                caregiverId, oldStartTime, oldEndTime);
        
        boolean hasModifiableConsultations = false;
        
        for (Consultation consultation : consultations) {
            // Only MODIFIED or REJECTED consultations allow schedule modification
            if (!"MODIFIED".equals(consultation.getStatus()) && !"REJECTED".equals(consultation.getStatus())) {
                log.warn("Cannot modify schedule with consultations in status {}: {} to {}", 
                        consultation.getStatus(), oldStartTime, oldEndTime);
                return false;
            }
            
            if ("MODIFIED".equals(consultation.getStatus())) {
                hasModifiableConsultations = true;
            }
        }
        
        boolean scheduleModified = scheduleRepository.modifyScheduleWithDateTime(
                caregiverId, oldStartTime, oldEndTime, newStartTime, newEndTime);
        
        if (!scheduleModified) {
            return false;
        }
        
        // Update consultations with MODIFIED status to use the new schedule and set to ACCEPTED
        if (hasModifiableConsultations) {
            for (Consultation consultation : consultations) {
                if ("MODIFIED".equals(consultation.getStatus())) {
                    consultation.setStartTime(newStartTime);
                    consultation.setEndTime(newEndTime);
                    consultation.setStatus("ACCEPTED");
                    consultationRepository.save(consultation);
                    log.info("Updated consultation schedule time and set status to ACCEPTED: {}", 
                            consultation.getId());
                }
            }
        }
        
        return true;
    }

    @Override
    public List<Map<String, Object>> findAvailableCaregivers(
            LocalDateTime startTime, LocalDateTime endTime, String specialty) {
        
        log.info("Finding available caregivers: startTime={}, endTime={}, specialty={}", 
                startTime, endTime, specialty);
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Get all caregivers
        List<Caregiver> allCaregivers = caregiverAdapter.findAll();
        
        // Filter by specialty if provided
        if (specialty != null && !specialty.isEmpty()) {
            allCaregivers = allCaregivers.stream()
                .filter(caregiver -> specialty.equals(caregiver.getSpecialty()))
                .collect(Collectors.toList());
        }
        
        log.info("Found {} caregivers matching specialty filter", allCaregivers.size());
        
        // Check if we're requesting a full day (more than 12 hours)
        boolean isFullDayRequest = Duration.between(startTime, endTime).toHours() >= 12;
        
        // Check each caregiver's availability
        for (Caregiver caregiver : allCaregivers) {
            String caregiverId = caregiver.getId();
            
            if (isFullDayRequest) {
                // For full day requests, get all available time slots for the day
                List<WorkingSchedule> availableSlots = workingScheduleRepository.findByCaregiverIdAndDateAndAvailable(
                        caregiverId, 
                        startTime.toLocalDate(),
                        true);
                
                // If caregiver has any available slots that day, include them
                if (!availableSlots.isEmpty()) {
                    Map<String, Object> caregiverInfo = new HashMap<>();
                    caregiverInfo.put("id", caregiverId);
                    caregiverInfo.put("name", caregiver.getName());
                    caregiverInfo.put("specialty", caregiver.getSpecialty());
                    caregiverInfo.put("email", caregiver.getEmail());
                    caregiverInfo.put("phone", caregiver.getPhone());
                    
                    // Add all available time slots
                    List<Map<String, Object>> slots = new ArrayList<>();
                    for (WorkingSchedule slot : availableSlots) {
                        Map<String, Object> slotInfo = new HashMap<>();
                        slotInfo.put("startTime", slot.getStartTime());
                        slotInfo.put("endTime", slot.getEndTime());
                        slots.add(slotInfo);
                    }
                    caregiverInfo.put("availableSlots", slots);
                    
                    result.add(caregiverInfo);
                }
            } else {
                // For specific time slot requests, find overlapping available schedules
                List<WorkingSchedule> overlappingSchedules = workingScheduleRepository
                    .findByCaregiversWithOverlappingTimeSlots(caregiverId, startTime, endTime);
                
                // Filter to only available schedules
                List<WorkingSchedule> availableSchedules = overlappingSchedules.stream()
                    .filter(s -> s.isAvailable() && "AVAILABLE".equals(s.getStatus()))
                    .collect(Collectors.toList());
                
                if (!availableSchedules.isEmpty()) {
                    // Get the first available schedule that overlaps
                    WorkingSchedule schedule = availableSchedules.get(0);
                    
                    Map<String, Object> caregiverInfo = new HashMap<>();
                    caregiverInfo.put("id", caregiverId);
                    caregiverInfo.put("name", caregiver.getName());
                    caregiverInfo.put("specialty", caregiver.getSpecialty());
                    caregiverInfo.put("email", caregiver.getEmail());
                    caregiverInfo.put("phone", caregiver.getPhone());
                    
                    // Include the actual schedule time
                    caregiverInfo.put("actualStartTime", schedule.getStartTime());
                    caregiverInfo.put("actualEndTime", schedule.getEndTime());
                    
                    result.add(caregiverInfo);
                }
            }
        }
        
        log.info("Found {} available caregivers", result.size());
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getCaregiverSchedulesFormatted(String caregiverId) {
        log.info("Getting formatted schedules for caregiver {}", caregiverId);
        
        List<WorkingSchedule> workingSchedules = workingScheduleRepository.findByCaregiverId(caregiverId);
        List<Map<String, Object>> formattedSchedules = new ArrayList<>();
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (WorkingSchedule ws : workingSchedules) {
            if (ws.getStartTime() != null && ws.getEndTime() != null) {
                Map<String, Object> scheduleMap = new HashMap<>();
                
                // Format the times as strings
                String startTimeStr = ws.getStartTime().format(displayFormatter);
                String endTimeStr = ws.getEndTime().format(displayFormatter);
                
                scheduleMap.put("id", ws.getId());
                scheduleMap.put("startTime", startTimeStr);
                scheduleMap.put("endTime", endTimeStr);
                scheduleMap.put("status", ws.getStatus());
                scheduleMap.put("available", ws.isAvailable());
                
                formattedSchedules.add(scheduleMap);
            }
        }
        
        return formattedSchedules;
    }
}