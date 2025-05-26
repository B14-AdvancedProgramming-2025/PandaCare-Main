package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {
    // id, nik, and email cannot be updated
    private String name;
    private String address;
    private String phone;
    private UserType type;

    // Pacilian only
    private List<String> medicalHistory;

    // Caregiver only
    private String specialty;
    private List<WorkingSchedule> workingSchedule;

    // only for context
    private String nik; 
    private String email;
}
