package id.ac.ui.cs.advprog.b14.pandacare.rating.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private String caregiverId;
    private String pacilianId;
    private int value;
    private String comment;
}
