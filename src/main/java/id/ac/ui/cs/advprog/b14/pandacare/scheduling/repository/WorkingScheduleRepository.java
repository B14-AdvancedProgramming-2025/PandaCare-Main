package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;

import java.util.List;
import java.util.Optional;

public interface WorkingScheduleRepository {
    Optional<WorkingSchedule> findByCaregiverIdAndSchedule(String caregiverId, String schedule);
    
    List<WorkingSchedule> findByCaregiverId(String caregiverId);
    
    boolean updateAvailability(String caregiverId, String schedule, boolean available, String status);
    
    WorkingSchedule save(WorkingSchedule workingSchedule);
}