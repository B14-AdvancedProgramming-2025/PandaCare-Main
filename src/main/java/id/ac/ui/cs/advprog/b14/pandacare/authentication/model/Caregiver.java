package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name= "caregivers")
@Getter @Setter
public class Caregiver extends User {
    @ElementCollection
    @CollectionTable(name = "caregiver_working_schedule", joinColumns = @JoinColumn(name = "caregiver_id"))
    @Column(name = "working_schedule")
    private List<String> workingSchedule;

    protected Caregiver() {}

    public Caregiver(String email, String password, String name, String nik, String address, String phone, List<String> workingSchedule) {
        super(email, password, name, nik, address, phone);
        this.workingSchedule = workingSchedule;
    }
}