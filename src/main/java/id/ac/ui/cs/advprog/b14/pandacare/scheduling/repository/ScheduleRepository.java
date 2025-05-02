package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

public interface ScheduleRepository {
    boolean saveSchedule(String caregiverId, String schedule);
    boolean isScheduleAvailable(String caregiverId, String schedule);
}