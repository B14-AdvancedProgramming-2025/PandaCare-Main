package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;

public abstract class DoctorRatingDecorator extends DoctorRating {
    protected DoctorRating decoratedRating;

    protected DoctorRatingDecorator(DoctorRating decoratedRating) {
        this.decoratedRating = decoratedRating;
    }

    @Override
    public String getId() {
        return decoratedRating.getId();
    }

    @Override
    public Caregiver getCaregiver() {
        return decoratedRating.getCaregiver();
    }

    @Override
    public void setCaregiver(Caregiver caregiver) {
        decoratedRating.setCaregiver(caregiver);
    }

    @Override
    public Pacilian getPacilian() {
        return decoratedRating.getPacilian();
    }

    @Override
    public void setPacilian(Pacilian pacilian) {
        decoratedRating.setPacilian(pacilian);
    }

    @Override
    public int getValue() {
        return decoratedRating.getValue();
    }

    @Override
    public void setValue(int value) {
        decoratedRating.setValue(value);
    }

    @Override
    public String getComment() {
        return decoratedRating.getComment();
    }

    @Override
    public void setComment(String comment) {
        decoratedRating.setComment(comment);
    }
}
