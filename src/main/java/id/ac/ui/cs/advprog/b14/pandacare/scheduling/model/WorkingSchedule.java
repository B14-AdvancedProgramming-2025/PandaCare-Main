package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "caregiver_working_schedule")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WorkingSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "caregiver_id")
    private String caregiverId;
    
    @Column(name = "working_schedule")
    private String schedule;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "available")
    private boolean available;
}