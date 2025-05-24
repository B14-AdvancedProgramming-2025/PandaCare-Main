package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository {
    boolean saveScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    boolean isScheduleAvailableByDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    boolean deleteScheduleWithDateTime(String caregiverId, LocalDateTime startTime, LocalDateTime endTime);
    boolean modifyScheduleWithDateTime(String caregiverId, 
                                    LocalDateTime oldStartTime, LocalDateTime oldEndTime, 
                                    LocalDateTime newStartTime, LocalDateTime newEndTime);
    List<String> getCaregiverSchedules(String caregiverId);
}