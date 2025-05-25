package id.ac.ui.cs.advprog.b14.pandacare.rating.dto;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import lombok.Getter;

@Getter
public class RatingResponse {
    private final String id;
    private final String caregiverId;
    private final String caregiverName;
    private final String pacilianId;
    private final String pacilianName;
    private final int value;
    private final String comment;

    public RatingResponse(DoctorRating rating) {
        this.id = rating.getId();
        this.caregiverId = rating.getCaregiver().getId();
        this.caregiverName = rating.getCaregiver().getName();
        this.pacilianId = rating.getPacilian().getId();
        this.pacilianName = rating.getPacilian().getName();
        this.value = rating.getValue();
        this.comment = rating.getComment();
    }
}
