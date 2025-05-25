package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;

public class ConcreteDoctorRating extends DoctorRatingDecorator {

    public ConcreteDoctorRating(DoctorRating decoratedRating) {
        super(decoratedRating);
    }

    @Override
    public void setValue(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5");
        }
        decoratedRating.setValue(value);
    }

    @Override
    public void setComment(String comment) {
        if (comment != null) {
            comment = comment.trim();
        }
        decoratedRating.setComment(comment);
    }
}
