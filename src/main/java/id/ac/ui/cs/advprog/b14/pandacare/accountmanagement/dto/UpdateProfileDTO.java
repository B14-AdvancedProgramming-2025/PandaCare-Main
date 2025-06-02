package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
// import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UpdateProfileDTO {
    // id, email, and nik are not included in the update
    private String name;
    private String address;
    private String phone;
    private UserType type;

    // Pacilian only
    private List<String> medicalHistory;

    // Caregiver only
    private String specialty;
    private List<String> workingSchedule;

    // only for context, not DTO fields
    private String nik; 
    private String email;
}
