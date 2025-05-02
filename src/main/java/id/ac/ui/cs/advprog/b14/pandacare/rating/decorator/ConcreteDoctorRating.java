package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;

public class ConcreteDoctorRating extends DoctorRatingDecorator {

    public ConcreteDoctorRating(DoctorRating decoratedRating) {
        super(decoratedRating);
    }

    @Override
    public void setValue(int value) {
        decoratedRating.setValue(value);
    }

    @Override
    public void setComment(String comment) {
        decoratedRating.setComment(comment);
    }
}
