package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Account {

    private UUID id;
    private String email;
    private String password;
    private String name;
    private String nik;
    private String address;
    private String phoneNumber;
    private String medicalHistory;
    private String speciality;
    private String workingSchedules;

    public Account(String email, String password, String name, String nik, String address,
                   String phoneNumber, String medicalHistory, String speciality, String workingSchedules) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        this.email = email;
        this.password = password;
        this.name = name;
        this.nik = nik;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.medicalHistory = medicalHistory;
        this.speciality = speciality;
        this.workingSchedules = workingSchedules;
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
