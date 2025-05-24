package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateProfileDTO {
    // id and nik are not updatable from DTO for profile updates
    // email is also not updatable as per AccountServiceImpl logic
    private String name;
    private String address;
    private String phone;
    private UserType type; // To identify if it's Pacilian or Caregiver for specific fields

    // Pacilian only
    private List<String> medicalHistory;

    // Caregiver only
    private String specialty;
    private List<WorkingSchedule> workingSchedule;

    // Add NIK and Email fields, but they won't be used for update, only for context if needed.
    // These are typically path variables or part of the security context, not DTO for update.
    private String nik; 
    private String email;
}
