package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "caregiver_id")
    private List<WorkingSchedule> workingSchedule;

    protected Caregiver() {}

    public Caregiver(String id, String email, String password, String name, String nik, String address, String phone, String specialty, List<WorkingSchedule> workingSchedule) {
        super(id, email, password, name, nik, address, phone, UserType.CAREGIVER);
        this.specialty = specialty;
        this.workingSchedule = workingSchedule;
    }
}