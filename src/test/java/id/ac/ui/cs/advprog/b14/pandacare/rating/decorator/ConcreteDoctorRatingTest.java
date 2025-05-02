package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ConcreteDoctorRatingTest {

    @Test
    public void testConcreteDoctorRatingGetters() {
        DoctorRating rating = new DoctorRating();
        Pacilian pacilian = new Pacilian("paci@test.com", "pass", "Paci Name", "1234", "Jl. A", "080000000", List.of());
        Caregiver caregiver = new Caregiver("care@test.com", "pass", "Care Name", "5678", "Jl. B", "081111111", "Psychology", List.of("Monday"));
        rating.setPacilian(pacilian);
        rating.setCaregiver(caregiver);
        rating.setValue(4);
        rating.setComment("Great service");

        ConcreteDoctorRating concreteRating = new ConcreteDoctorRating(rating);

        assertEquals(pacilian, concreteRating.getPacilian());
        assertEquals(caregiver, concreteRating.getCaregiver());
        assertEquals(4, concreteRating.getValue());
        assertEquals("Great service", concreteRating.getComment());
    }
}