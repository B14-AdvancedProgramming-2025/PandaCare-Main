package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "working_schedules")
@Getter @Setter @NoArgsConstructor
public class WorkingSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "caregiver_id", nullable = false)
    private String caregiverId;
    
    // Keeping the schedule field for backward compatibility, but it will be deprecated
    @Column(nullable = true)
    private String schedule;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private boolean available;
    
    // Constructor for backward compatibility
    public WorkingSchedule(Long id, String caregiverId, String schedule, String status, boolean available) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.schedule = schedule;
        this.status = status;
        this.available = available;
    }
    
    // New constructor with DateTime
    public WorkingSchedule(Long id, String caregiverId, LocalDateTime startTime, LocalDateTime endTime, 
                        String status, boolean available) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.available = available;
    }
}