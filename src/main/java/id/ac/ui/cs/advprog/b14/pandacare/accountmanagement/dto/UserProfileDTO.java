package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserProfileDTO {
    private String id;
    private String email;
    private String name;
    private String nik;
    private String address;
    private String phone;
    private UserType type;

    // Pacilian
    private List<String> medicalHistory;

    // Caregiver
    private String specialty;
    private List<WorkingScheduleDTO> workingSchedule;

    private List<ConsultationDTO> consultationHistory;
}
