package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import java.util.List;

public class UpdateProfileDTO {
    private String id;
    private String email;
    private UserType type;

    private String name;
    private String address;
    private String phone;

    // Pacilian only
    private List<String> medicalHistory;

    // Caregiver only
    private String specialty;
    private List<String> workingSchedule;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserType getType() { return type; }
    public void setType(UserType type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<String> getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(List<String> medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public List<String> getWorkingSchedule() { return workingSchedule; }
    public void setWorkingSchedule(List<String> workingSchedule) { this.workingSchedule = workingSchedule; }
}
