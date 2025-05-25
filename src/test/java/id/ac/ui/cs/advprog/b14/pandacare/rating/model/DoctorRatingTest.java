package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorRatingTest {

    private DoctorRating doctorRating;
    private Caregiver caregiverMock;
    private Pacilian pacilianMock;

    @BeforeEach
    public void setUp() {
        caregiverMock = mock(Caregiver.class);
        when(caregiverMock.getId()).thenReturn("caregiver-123");

        pacilianMock = mock(Pacilian.class);
        when(pacilianMock.getId()).thenReturn("pacilian-456");

        doctorRating = new DoctorRating();
    }

    @Test
    public void testUUIDIsGeneratedInConstructor() {
        DoctorRating rating = new DoctorRating(caregiverMock, pacilianMock, 4, "Great doctor!");
        assertNotNull(rating.getId());
        assertFalse(rating.getId().isEmpty());
    }

    @Test
    public void testSetAndGetCaregiver() {
        doctorRating.setCaregiver(caregiverMock);
        assertEquals(caregiverMock, doctorRating.getCaregiver());
        assertEquals("caregiver-123", doctorRating.getCaregiver().getId());
    }

    @Test
    public void testSetAndGetPacilian() {
        doctorRating.setPacilian(pacilianMock);
        assertEquals(pacilianMock, doctorRating.getPacilian());
        assertEquals("pacilian-456", doctorRating.getPacilian().getId());
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
        doctorRating.setCaregiver(caregiverMock);
        doctorRating.setPacilian(pacilianMock);
        doctorRating.setValue(3);
        doctorRating.setComment("Average service.");

        assertAll("DoctorRating Full Test",
                () -> assertEquals(caregiverMock, doctorRating.getCaregiver()),
                () -> assertEquals("caregiver-123", doctorRating.getCaregiver().getId()),
                () -> assertEquals(pacilianMock, doctorRating.getPacilian()),
                () -> assertEquals("pacilian-456", doctorRating.getPacilian().getId()),
                () -> assertEquals(3, doctorRating.getValue()),
                () -> assertEquals("Average service.", doctorRating.getComment())
        );
    }
}
