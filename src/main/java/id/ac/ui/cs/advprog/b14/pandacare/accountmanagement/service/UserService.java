package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.CaregiverUpdateDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.WorkingScheduleDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper; // Autowire ObjectMapper

    private static final DateTimeFormatter DTO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email); // Corrected: findByEmail returns User directly
    }

    public User findUserById(String userId) { // Corrected: ID type is String
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUserProfile(String userId, String userType, Map<String, Object> updates) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update common fields
        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }
        if (updates.containsKey("address")) {
            user.setAddress((String) updates.get("address"));
        }
        if (updates.containsKey("phone")) {
            user.setPhone((String) updates.get("phone"));
        }

        if (UserType.valueOf(userType) == UserType.PACILIAN && user instanceof Pacilian) {
            Pacilian pacilian = (Pacilian) user;
            if (updates.containsKey("medicalHistory")) {
                // Assuming medicalHistory is sent as a List of Strings
                pacilian.setMedicalHistory((List<String>) updates.get("medicalHistory"));
            }
        } else if (UserType.valueOf(userType) == UserType.CAREGIVER && user instanceof Caregiver) {
            Caregiver caregiver = (Caregiver) user;
            if (updates.containsKey("specialty")) {
                caregiver.setSpecialty((String) updates.get("specialty"));
            }
            if (updates.containsKey("workingSchedule")) {
                List<Map<String, Object>> wsDataList = (List<Map<String, Object>>) updates.get("workingSchedule");
                List<WorkingSchedule> finalWorkingScheduleList = new ArrayList<>();

                if (wsDataList != null) {
                    // Create a mutable map of existing schedules to update
                    Map<Long, WorkingSchedule> existingSchedulesMap = caregiver.getWorkingSchedule()
                            .stream()
                            .collect(Collectors.toMap(
                                WorkingSchedule::getId,
                                ws -> ws,
                                (ws1, ws2) -> ws1 // Handle potential duplicates if any, though IDs should be unique
                            ));

                    for (Map<String, Object> scheduleMap : wsDataList) {
                        WorkingScheduleDTO dto = objectMapper.convertValue(scheduleMap, WorkingScheduleDTO.class);
                        WorkingSchedule scheduleEntity;

                        if (dto.getId() != null && existingSchedulesMap.containsKey(dto.getId())) {
                            // Existing schedule: update it
                            scheduleEntity = existingSchedulesMap.get(dto.getId());
                            existingSchedulesMap.remove(dto.getId()); // Mark as processed
                        } else {
                            // New schedule
                            scheduleEntity = new WorkingSchedule();
                            scheduleEntity.setCaregiverId(caregiver.getId()); // Associate with caregiver
                            if (dto.getId() != null) {
                                scheduleEntity.setId(dto.getId()); // Set ID if provided
                            }
                        }

                        // Populate/update entity fields from DTO
                        if (dto.getStartTime() != null && !dto.getStartTime().isEmpty()) {
                            scheduleEntity.setStartTime(LocalDateTime.parse(dto.getStartTime(), DTO_DATE_TIME_FORMATTER));
                        } else {
                            scheduleEntity.setStartTime(null); // Allow clearing the field
                        }
                        if (dto.getEndTime() != null && !dto.getEndTime().isEmpty()) {
                            scheduleEntity.setEndTime(LocalDateTime.parse(dto.getEndTime(), DTO_DATE_TIME_FORMATTER));
                        } else {
                            scheduleEntity.setEndTime(null); // Allow clearing the field
                        }
                        scheduleEntity.setStatus(dto.getStatus());
                        scheduleEntity.setAvailable(dto.isAvailable());
                        // If WorkingSchedule has a bidirectional @ManyToOne to Caregiver, e.g. `private Caregiver caregiver;`
                        // ensure it's set: scheduleEntity.setCaregiver(caregiver);

                        finalWorkingScheduleList.add(scheduleEntity);
                    }
                }

                // After processing all DTOs:
                // - finalWorkingScheduleList contains all schedules that should be associated with the caregiver (updated existing ones + new ones).
                // - Schedules remaining in existingSchedulesMap.values() are those that were originally on the caregiver
                //   but were NOT in the DTO list. These should be removed.
                // By clearing the caregiver's current collection and adding all from finalWorkingScheduleList,
                // JPA (with orphanRemoval=true configured on Caregiver.workingSchedule) will delete the schedules
                // that were in existingSchedulesMap (and thus effectively removed from the collection).
                caregiver.getWorkingSchedule().clear();
                caregiver.getWorkingSchedule().addAll(finalWorkingScheduleList);
            }
        }
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
