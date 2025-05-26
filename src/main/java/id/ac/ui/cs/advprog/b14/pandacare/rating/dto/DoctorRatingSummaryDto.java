package id.ac.ui.cs.advprog.b14.pandacare.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorRatingSummaryDto {
    private String caregiverId;
    private String caregiverName;
    private double averageRating;
}
