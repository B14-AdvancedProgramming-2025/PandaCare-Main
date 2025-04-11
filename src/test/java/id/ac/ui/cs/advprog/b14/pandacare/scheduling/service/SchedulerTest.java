package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Appointment;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.AppointmentRequest;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    @Test
    void testSchedulerUsesStrategy() {
        SchedulingStrategy strategy = new DummyStrategy();
        Scheduler scheduler = new Scheduler(strategy);
        AppointmentRequest request = new AppointmentRequest();
        Appointment result = scheduler.schedule(request);

        assertNull(result);
    }

    class DummyStrategy implements SchedulingStrategy {
        @Override
        public Appointment schedule(AppointmentRequest request) {
            return null;
        }
    }
}
