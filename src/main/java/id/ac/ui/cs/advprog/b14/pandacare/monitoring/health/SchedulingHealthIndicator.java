package id.ac.ui.cs.advprog.b14.pandacare.monitoring.health;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import org.springframework.stereotype.Component;

@Component
public class SchedulingHealthIndicator {

    private final ConsultationRepository consultationRepository;
    private final WorkingScheduleRepository workingScheduleRepository;

    public SchedulingHealthIndicator(ConsultationRepository consultationRepository,
                                   WorkingScheduleRepository workingScheduleRepository) {
        this.consultationRepository = consultationRepository;
        this.workingScheduleRepository = workingScheduleRepository;
    }

    public boolean isHealthy() {
        try {
            consultationRepository.count();
            workingScheduleRepository.count();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getHealthStatus() {
        try {
            long consultationCount = consultationRepository.count();
            long scheduleCount = workingScheduleRepository.count();
            return String.format("Healthy - Consultations: %d, Schedules: %d", consultationCount, scheduleCount);
        } catch (Exception e) {
            return "Unhealthy - " + e.getMessage();
        }
    }
}