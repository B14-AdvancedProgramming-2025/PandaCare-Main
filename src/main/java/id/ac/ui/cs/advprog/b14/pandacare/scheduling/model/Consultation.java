package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@Getter @Setter @NoArgsConstructor
public class Consultation {
    
    @Id
    private String id;
    
    @Column(name = "caregiver_id", nullable = false)
    private String caregiverId;
    
    @Column(name = "pacilian_id", nullable = false)
    private String pacilianId;
    
    // Keeping the scheduleTime field for backward compatibility, but it will be deprecated
    @Column(name = "schedule_time", nullable = true)
    private String scheduleTime;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false) 
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private String status;
    
    // Constructor for backward compatibility
    public Consultation(String id, String caregiverId, String pacilianId, String scheduleTime, String status) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.pacilianId = pacilianId;
        this.scheduleTime = scheduleTime;
        this.status = status;
    }
    
    // New constructor with DateTime
    public Consultation(String id, String caregiverId, String pacilianId, 
                        LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.pacilianId = pacilianId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}