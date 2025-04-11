package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

public class DoctorRating {
    private Doctor doctor;
    private User user;
    private int value;
    private String comment;

    // Getters & Setters
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}

