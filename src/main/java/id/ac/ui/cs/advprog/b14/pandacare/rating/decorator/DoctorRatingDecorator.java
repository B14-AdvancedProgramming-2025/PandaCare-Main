package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;

public abstract class DoctorRatingDecorator extends DoctorRating {
    protected DoctorRating decoratedRating;

    public DoctorRatingDecorator(DoctorRating decoratedRating) {
        super(decoratedRating.getCaregiverId(), decoratedRating.getPacilianId(),
                decoratedRating.getValue(), decoratedRating.getComment());
        this.setId(decoratedRating.getId());
        this.decoratedRating = decoratedRating;
    }

    // Abstractoleh subclass
    public abstract void setValue(int value);
    public abstract void setComment(String comment);

    // Getters
    @Override
    public int getValue() {
        return decoratedRating.getValue();
    }

    @Override
    public String getComment() {
        return decoratedRating.getComment();
    }

    @Override
    public String getId() {
        return decoratedRating.getId();
    }

    @Override
    public String getCaregiverId() {
        return decoratedRating.getCaregiverId();
    }

    @Override
    public String getPacilianId() {
        return decoratedRating.getPacilianId();
    }
}
