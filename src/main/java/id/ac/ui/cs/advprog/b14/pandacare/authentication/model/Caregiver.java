package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name= "caregivers")
@Getter @Setter
@SuperBuilder
public class Caregiver extends User {
    @Column(nullable = false)
    private String specialty;

    @ElementCollection
    @CollectionTable(name = "caregiver_working_schedule", joinColumns = @JoinColumn(name = "caregiver_id"))
    @Column(name = "working_schedule")
    private List<String> workingSchedule;

    protected Caregiver() {}

    public Caregiver(String email, String password, String name, String nik, String address, String phone, String specialty, List<String> workingSchedule) {
        super(email, password, name, nik, address, phone);
        this.specialty = specialty;
        this.workingSchedule = workingSchedule;
    }
}