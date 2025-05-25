package id.ac.ui.cs.advprog.b14.pandacare.monitoring.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfig {

    @Bean
    public Counter consultationBookedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("pandacare.consultations.booked")
                .description("Number of consultations booked")
                .register(meterRegistry);
    }

    @Bean
    public Counter consultationAcceptedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("pandacare.consultations.accepted")
                .description("Number of consultations accepted")
                .register(meterRegistry);
    }

    @Bean
    public Counter consultationRejectedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("pandacare.consultations.rejected")
                .description("Number of consultations rejected")
                .register(meterRegistry);
    }

    @Bean
    public Counter scheduleCreatedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("pandacare.schedules.created")
                .description("Number of schedules created")
                .register(meterRegistry);
    }

    @Bean
    public Timer consultationBookingTimer(MeterRegistry meterRegistry) {
        return Timer.builder("pandacare.consultations.booking.duration")
                .description("Time taken to book consultations")
                .register(meterRegistry);
    }

    @Bean
    public Timer scheduleCreationTimer(MeterRegistry meterRegistry) {
        return Timer.builder("pandacare.schedules.creation.duration")
                .description("Time taken to create schedules")
                .register(meterRegistry);
    }
}