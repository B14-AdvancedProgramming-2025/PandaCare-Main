package id.ac.ui.cs.advprog.b14.pandacare.monitoring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

    private static final Logger log = LoggerFactory.getLogger(MonitoringService.class);
    
    private final Counter consultationBookedCounter;
    private final Counter consultationAcceptedCounter;
    private final Counter consultationRejectedCounter;
    private final Counter scheduleCreatedCounter;
    private final Timer consultationBookingTimer;
    private final Timer scheduleCreationTimer;
    private final MeterRegistry meterRegistry;

    public MonitoringService(
            Counter consultationBookedCounter,
            Counter consultationAcceptedCounter,
            Counter consultationRejectedCounter,
            Counter scheduleCreatedCounter,
            Timer consultationBookingTimer,
            Timer scheduleCreationTimer,
            MeterRegistry meterRegistry) {
        this.consultationBookedCounter = consultationBookedCounter;
        this.consultationAcceptedCounter = consultationAcceptedCounter;
        this.consultationRejectedCounter = consultationRejectedCounter;
        this.scheduleCreatedCounter = scheduleCreatedCounter;
        this.consultationBookingTimer = consultationBookingTimer;
        this.scheduleCreationTimer = scheduleCreationTimer;
        this.meterRegistry = meterRegistry;
    }

    public void recordConsultationBooked() {
        consultationBookedCounter.increment();
        log.debug("Consultation booked metric recorded");
    }

    public void recordConsultationAccepted() {
        consultationAcceptedCounter.increment();
        log.debug("Consultation accepted metric recorded");
    }

    public void recordConsultationRejected() {
        consultationRejectedCounter.increment();
        log.debug("Consultation rejected metric recorded");
    }

    public void recordScheduleCreated() {
        scheduleCreatedCounter.increment();
        log.debug("Schedule created metric recorded");
    }

    public Timer.Sample startConsultationBookingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopConsultationBookingTimer(Timer.Sample sample) {
        sample.stop(consultationBookingTimer);
        log.debug("Consultation booking duration recorded");
    }

    public Timer.Sample startScheduleCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopScheduleCreationTimer(Timer.Sample sample) {
        sample.stop(scheduleCreationTimer);
        log.debug("Schedule creation duration recorded");
    }

    public void recordCustomMetric(String name, String description, double value) {
        meterRegistry.gauge(name, value);
        log.debug("Custom metric recorded: {} = {}", name, value);
    }
}