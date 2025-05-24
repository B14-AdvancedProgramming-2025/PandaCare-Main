package id.ac.ui.cs.advprog.b14.pandacare.authentication.dto;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RegisterRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "NIK cannot be empty")
    private String nik;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    private List<String> medicalHistory;

    private String specialty;
    private List<WorkingSchedule> workingSchedule;
}