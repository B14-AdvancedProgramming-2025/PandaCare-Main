package id.ac.ui.cs.advprog.b14.pandacare.scheduling.service;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Appointment;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.AppointmentRequest;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy.SchedulingStrategy;

public class Scheduler {
    private final SchedulingStrategy strategy;

    public Scheduler(SchedulingStrategy strategy) {
        this.strategy = strategy;
    }

    public Appointment schedule(AppointmentRequest request) {
        return strategy.schedule(request);
    }
}
