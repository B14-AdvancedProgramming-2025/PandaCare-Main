package id.ac.ui.cs.advprog.b14.pandacare.rating.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorRatingTest {

    @Test
    public void testCreateRating() {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Rina");

        User user = new User();
        user.setUsername("user1");

        DoctorRating rating = new DoctorRating();
        rating.setDoctor(doctor);
        rating.setUser(user);
        rating.setValue(4);
        rating.setComment("Pelayanan sangat baik!");

        assertEquals("Dr. Rina", rating.getDoctor().getName());
        assertEquals("user1", rating.getUser().getUsername());
        assertEquals(4, rating.getValue());
        assertEquals("Pelayanan sangat baik!", rating.getComment());
    }
}
