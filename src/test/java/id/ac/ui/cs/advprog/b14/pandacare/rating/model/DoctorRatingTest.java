package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorRatingTest {

    private DoctorRating doctorRating;
    private Pacilian pacilian;
    private Caregiver caregiver;

    @BeforeEach
    public void setUp() {
        pacilian = new Pacilian(
                "paci@gmail.com", "p@ss", "Paci Test", "1234567890",
                "Jl. Paci", "08123456789", List.of("Alergi")
        );

        caregiver = new Caregiver(
                "care@gmail.com", "c@re", "Caregiver Test", "0987654321",
                "Jl. Care", "08987654321", "Umum", List.of("Senin", "Selasa")
        );

        doctorRating = new DoctorRating();
    }

    @Test
    public void testSetAndGetPacilian() {
        doctorRating.setPacilian(pacilian);
        assertEquals(pacilian, doctorRating.getPacilian());
    }

    @Test
    public void testSetAndGetCaregiver() {
        doctorRating.setCaregiver(caregiver);
        assertEquals(caregiver, doctorRating.getCaregiver());
    }

    @Test
    public void testSetAndGetValue() {
        doctorRating.setValue(4);
        assertEquals(4, doctorRating.getValue());
    }

    @Test
    public void testSetAndGetComment() {
        String comment = "Dokternya sangat ramah dan membantu!";
        doctorRating.setComment(comment);
        assertEquals(comment, doctorRating.getComment());
    }

    @Test
    public void testFullObject() {
        doctorRating.setPacilian(pacilian);
        doctorRating.setCaregiver(caregiver);
        doctorRating.setValue(5);
        doctorRating.setComment("Excellent service!");

        assertAll("DoctorRating Full Test",
                () -> assertEquals(pacilian, doctorRating.getPacilian()),
                () -> assertEquals(caregiver, doctorRating.getCaregiver()),
                () -> assertEquals(5, doctorRating.getValue()),
                () -> assertEquals("Excellent service!", doctorRating.getComment())
        );
    }
}
