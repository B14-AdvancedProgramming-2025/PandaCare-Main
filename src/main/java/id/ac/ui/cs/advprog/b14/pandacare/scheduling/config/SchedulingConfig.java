package id.ac.ui.cs.advprog.b14.pandacare.scheduling.config;

import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.SchedulingContext;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.adapter.CaregiverRepositoryAdapter;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.DefaultSchedulingStrategy;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SchedulingConfig {
    
    @Bean
    public SchedulingContext schedulingContext(SchedulingStrategy strategy) {
        SchedulingContext context = new SchedulingContext();
        context.setStrategy(strategy);
        return context;
    }
    
    @Primary
    @Bean
    public SchedulingStrategy schedulingStrategy(
            ScheduleRepository scheduleRepository,
            ConsultationRepository consultationRepository,
            WorkingScheduleRepository workingScheduleRepository,
            ConsultationService consultationService,
            CaregiverRepositoryAdapter caregiverAdapter,
            ChatService chatService) {
        return new DefaultSchedulingStrategy(
                scheduleRepository,
                consultationRepository,
                workingScheduleRepository,
                consultationService,
                caregiverAdapter,
                chatService
        );
    }
}