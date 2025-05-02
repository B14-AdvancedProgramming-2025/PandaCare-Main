package id.ac.ui.cs.advprog.b14.pandacare.scheduling.config;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulingConfig {
    
    @Bean
    public SchedulingContext schedulingContext() {
        return new SchedulingContext();
    }
    
    @Bean
    public DefaultSchedulingStrategy defaultSchedulingStrategy(
            ScheduleRepository scheduleRepository,
            ConsultationRepository consultationRepository) {
        return new DefaultSchedulingStrategy(scheduleRepository, consultationRepository);
    }
}