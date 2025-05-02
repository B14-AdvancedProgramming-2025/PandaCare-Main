package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name= "pacilians")
@Getter @Setter
public class Pacilian extends User {
    @ElementCollection
    @CollectionTable(name = "pacilian_medical_history", joinColumns = @JoinColumn(name = "pacilian_id"))
    @Column(name = "medical_history")
    private List<String> medicalHistory;

    protected Pacilian() {}

    public Pacilian(String email, String password, String name, String nik, String address, String phone, List<String> medicalHistory) {
        super(email, password, name, nik, address, phone);
        this.medicalHistory = medicalHistory;
    }
}