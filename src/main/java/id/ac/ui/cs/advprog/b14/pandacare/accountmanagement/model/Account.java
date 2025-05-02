package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    // Common fields (User base class)
    private UUID id;
    private String email;
    private String password;
    private String name;
    private String nik;
    private String address;
    private String phoneNumber;

    // UserType: "PACILIAN" or "CAREGIVER"
    private String userType;

    // Only for Pacilian
    private List<String> medicalHistory;

    // Only for Caregiver
    private String specialty;
    private List<String> workingSchedules;
}
