package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name= "caregivers")
@Getter @Setter
public class Caregiver extends User {
    @Column(nullable = false)
    private String specialty;

    @ElementCollection
    @CollectionTable(name = "caregiver_working_schedule", joinColumns = @JoinColumn(name = "caregiver_id"))
    @Column(name = "working_schedule")
    private List<String> workingSchedule;

    protected Caregiver() {}

    public Caregiver(String id, String email, String password, String name, String nik, String address, String phone, String specialty, List<String> workingSchedule) {
        super(id, email, password, name, nik, address, phone, UserType.CAREGIVER);
        this.specialty = specialty;
        this.workingSchedule = workingSchedule;
    }
}