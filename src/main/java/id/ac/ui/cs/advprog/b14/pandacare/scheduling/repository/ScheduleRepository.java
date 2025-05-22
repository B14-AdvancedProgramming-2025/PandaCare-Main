package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository {
    // Legacy methods for backward compatibility
    boolean saveSchedule(String caregiverId, String schedule);
    boolean isScheduleAvailable(String caregiverId, String schedule);
    boolean deleteSchedule(String caregiverId, String schedule);
    boolean modifySchedule(String caregiverId, String oldSchedule, String newSchedule);
    List<String> getCaregiverSchedules(String caregiverId);
    
    // New methods with DateTime
    boolean saveScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    boolean isScheduleAvailableByDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    boolean modifyScheduleWithDateTime(String caregiverId, 
                                    LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                    LocalDateTime newStartTime, LocalDateTime newEndTime);
}