package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;

public abstract class DoctorRatingDecorator extends DoctorRating {
    protected DoctorRating decoratedRating;

    public DoctorRatingDecorator(DoctorRating decoratedRating) {
        this.decoratedRating = decoratedRating;
    }

    public abstract void setValue(int value);
    public abstract void setComment(String comment);
}
