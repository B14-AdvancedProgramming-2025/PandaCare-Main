package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConsultationDTO {
    private LocalDateTime consultationTime;
    private String partnerName;
    private String notes;
}
