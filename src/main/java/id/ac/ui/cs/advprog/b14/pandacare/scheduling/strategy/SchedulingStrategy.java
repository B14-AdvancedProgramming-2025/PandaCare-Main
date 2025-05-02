package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import java.util.List;

public interface SchedulingStrategy {
    boolean isScheduleAvailable(String proposedSchedule, List<String> existingSchedules);
}