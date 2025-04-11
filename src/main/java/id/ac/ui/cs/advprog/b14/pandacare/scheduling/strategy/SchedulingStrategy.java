package id.ac.ui.cs.advprog.b14.pandacare.scheduling.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Appointment;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.AppointmentRequest;

public interface SchedulingStrategy {
    Appointment schedule(AppointmentRequest request);
}
