package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DoctorRating {
    private String id;
    private String caregiverId;
    private String pacilianId;
    private int value;
    private String comment;

    public DoctorRating(String caregiverId, String pacilianId, int value, String comment) {
        this.id = UUID.randomUUID().toString();  // generate ID otomatis
        this.caregiverId = caregiverId;
        this.pacilianId = pacilianId;
        this.value = value;
        this.comment = comment;
    }
}
