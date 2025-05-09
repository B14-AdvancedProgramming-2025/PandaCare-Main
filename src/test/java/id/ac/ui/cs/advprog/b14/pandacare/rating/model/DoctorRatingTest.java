package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorRatingTest {

    private DoctorRating doctorRating;

    @BeforeEach
    public void setUp() {
        doctorRating = new DoctorRating();
    }

    @Test
    public void testUUIDIsGeneratedInConstructor() {
        DoctorRating rating = new DoctorRating("caregiver-001", "pacilian-001", 4, "Great doctor!");
        assertNotNull(rating.getId());
        assertFalse(rating.getId().isEmpty());
    }

    @Test
    public void testSetAndGetCaregiverId() {
        doctorRating.setCaregiverId("caregiver-123");
        assertEquals("caregiver-123", doctorRating.getCaregiverId());
    }

    @Test
    public void testSetAndGetPacilianId() {
        doctorRating.setPacilianId("pacilian-456");
        assertEquals("pacilian-456", doctorRating.getPacilianId());
    }

    @Test
    public void testSetAndGetValue() {
        doctorRating.setValue(5);
        assertEquals(5, doctorRating.getValue());
    }

    @Test
    public void testSetAndGetComment() {
        String comment = "Very helpful and kind!";
        doctorRating.setComment(comment);
        assertEquals(comment, doctorRating.getComment());
    }

    @Test
    public void testFullObject() {
        doctorRating.setCaregiverId("caregiver-xyz");
        doctorRating.setPacilianId("pacilian-xyz");
        doctorRating.setValue(3);
        doctorRating.setComment("Average service.");

        assertAll("DoctorRating Full Test",
                () -> assertEquals("caregiver-xyz", doctorRating.getCaregiverId()),
                () -> assertEquals("pacilian-xyz", doctorRating.getPacilianId()),
                () -> assertEquals(3, doctorRating.getValue()),
                () -> assertEquals("Average service.", doctorRating.getComment())
        );
    }
}
