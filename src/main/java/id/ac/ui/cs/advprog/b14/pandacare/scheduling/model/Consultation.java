package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consultations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Consultation {
    
    @Id
    private String id;
    
    @Column(name = "caregiver_id", nullable = false)
    private String caregiverId;
    
    @Column(name = "pacilian_id", nullable = false)
    private String pacilianId;
    
    @Column(name = "schedule_time", nullable = false)
    private String scheduleTime;
    
    @Column(nullable = false)
    private String status;
}