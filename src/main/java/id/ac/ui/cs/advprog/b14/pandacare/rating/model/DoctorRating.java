package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "doctor_ratings")
@Getter
@Setter
@NoArgsConstructor
public class DoctorRating {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @ManyToOne
    @JoinColumn(name = "pacilian_id", nullable = false)
    private Pacilian pacilian;

    private int value;
    private String comment;

    public DoctorRating(Caregiver caregiver, Pacilian pacilian, int value, String comment) {
        this.id = UUID.randomUUID().toString();
        this.caregiver = caregiver;
        this.pacilian = pacilian;
        this.value = value;
        this.comment = comment;
    }
}