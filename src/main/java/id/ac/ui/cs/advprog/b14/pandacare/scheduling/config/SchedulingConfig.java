package id.ac.ui.cs.advprog.b14.pandacare.scheduling.config;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulingConfig {
    
    @Bean
    public SchedulingContext schedulingContext(DefaultSchedulingStrategy defaultSchedulingStrategy) {
        SchedulingContext context = new SchedulingContext();
        context.setStrategy(defaultSchedulingStrategy);
        return context;
    }
    
    @Bean
    public DefaultSchedulingStrategy defaultSchedulingStrategy(
            ScheduleRepository scheduleRepository,
            ConsultationRepository consultationRepository,
            WorkingScheduleRepository workingScheduleRepository,
            ConsultationService consultationService,
            CaregiverRepositoryAdapter caregiverAdapter) {
        return new DefaultSchedulingStrategy(
                scheduleRepository, 
                consultationRepository, 
                workingScheduleRepository,
                consultationService,
                caregiverAdapter);
    }
}