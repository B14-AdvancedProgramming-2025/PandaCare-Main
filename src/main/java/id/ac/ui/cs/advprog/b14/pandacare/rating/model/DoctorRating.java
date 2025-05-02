package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;

public class DoctorRating {
    private Caregiver caregiver; // yang diberi rating
    private Pacilian pacilian;   // yang memberi rating
    private int value;
    private String comment;

    // Getters & Setters
    public Caregiver getCaregiver() { return caregiver; }
    public void setCaregiver(Caregiver caregiver) { this.caregiver = caregiver; }

    public Pacilian getPacilian() { return pacilian; }
    public void setPacilian(Pacilian pacilian) { this.pacilian = pacilian; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
