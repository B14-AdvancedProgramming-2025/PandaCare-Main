package id.ac.ui.cs.advprog.b14.pandacare.rating.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RatingResponseTest {

    @Test
    void testRatingResponseMapping_withMockedCaregiverAndPacilian() {
        // Mock Caregiver
        Caregiver caregiver = mock(Caregiver.class);
        when(caregiver.getId()).thenReturn("c1");
        when(caregiver.getName()).thenReturn("Dr. John");

        // Mock Pacilian
        Pacilian pacilian = mock(Pacilian.class);
        when(pacilian.getId()).thenReturn("p1");
        when(pacilian.getName()).thenReturn("Patient A");

        // Create DoctorRating object
        DoctorRating rating = new DoctorRating();
        rating.setId("r1");
        rating.setCaregiver(caregiver);
        rating.setPacilian(pacilian);
        rating.setValue(4);
        rating.setComment("Helpful");

        // Convert to DTO
        RatingResponse response = new RatingResponse(rating);

        // Assertions
        assertEquals("r1", response.getId());
        assertEquals("c1", response.getCaregiverId());
        assertEquals("Dr. John", response.getCaregiverName());
        assertEquals("p1", response.getPacilianId());
        assertEquals("Patient A", response.getPacilianName());
        assertEquals(4, response.getValue());
        assertEquals("Helpful", response.getComment());
    }
}
