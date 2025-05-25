package id.ac.ui.cs.advprog.b14.pandacare.rating.decorator;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConcreteDoctorRatingTest {

    private Caregiver caregiver;
    private Pacilian pacilian;

    @BeforeEach
    public void setUp() {
        caregiver = mock(Caregiver.class);
        when(caregiver.getId()).thenReturn("caregiver-001");

        pacilian = mock(Pacilian.class);
        when(pacilian.getId()).thenReturn("pacilian-001");
    }

    @Test
    public void testConcreteDoctorRatingSettersAndGetters() {
        // Arrange
        DoctorRating baseRating = new DoctorRating(caregiver, pacilian, 3, "Good service");
        ConcreteDoctorRating concreteRating = new ConcreteDoctorRating(baseRating);

        // Act
        concreteRating.setValue(5);
        concreteRating.setComment("Excellent service!");

        // Assert
        assertEquals("caregiver-001", concreteRating.getCaregiver().getId());
        assertEquals("pacilian-001", concreteRating.getPacilian().getId());
        assertEquals(5, concreteRating.getValue());
        assertEquals("Excellent service!", concreteRating.getComment());
    }

    @Test
    public void testSetValueOutOfRangeThrowsException() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 3, "Test");
        ConcreteDoctorRating concreteRating = new ConcreteDoctorRating(rating);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            concreteRating.setValue(10); // invalid value
        });

        assertEquals("Rating value must be between 1 and 5", exception.getMessage());
    }

    @Test
    public void testTrimmedComment() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 3, "Test");
        ConcreteDoctorRating concreteRating = new ConcreteDoctorRating(rating);

        concreteRating.setComment("   Nice doctor   ");
        assertEquals("Nice doctor", concreteRating.getComment());
    }
}
