package id.ac.ui.cs.advprog.b14.pandacare.scheduling.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "caregiver_working_schedule")
@Getter @Setter @NoArgsConstructor
public class WorkingSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "caregiver_id", nullable = false)
    private String caregiverId;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private boolean available;
    
    
    public WorkingSchedule(Long id, String caregiverId, LocalDateTime startTime, LocalDateTime endTime, 
                        String status, boolean available) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.available = available;
    }

    public String getFormattedSchedule() {
        if (startTime != null && endTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return startTime.format(formatter) + "-" + endTime.format(formatter);
        }
        return null;
    }
}