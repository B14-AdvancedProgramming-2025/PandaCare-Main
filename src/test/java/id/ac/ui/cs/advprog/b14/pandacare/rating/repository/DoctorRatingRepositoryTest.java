package id.ac.ui.cs.advprog.b14.pandacare.rating.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorRatingRepositoryTest {

    private DoctorRatingRepository repository;
    private Caregiver caregiver;
    private Pacilian pacilian;

    @BeforeEach
    public void setUp() {
        repository = new DoctorRatingRepository();

        caregiver = mock(Caregiver.class);
        when(caregiver.getId()).thenReturn("caregiver-001");

        pacilian = mock(Pacilian.class);
        when(pacilian.getId()).thenReturn("pacilian-001");
    }

    @Test
    public void testSaveAndFindById() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 5, "Good service");
        repository.save(rating);

        DoctorRating result = repository.findById(rating.getId());
        assertNotNull(result);
        assertEquals(caregiver, result.getCaregiver());
        assertEquals(pacilian, result.getPacilian());
        assertEquals(5, result.getValue());
        assertEquals("Good service", result.getComment());
    }

    @Test
    public void testFindAllReturnsAllRatings() {
        DoctorRating rating1 = new DoctorRating(caregiver, pacilian, 4, "Okay");
        DoctorRating rating2 = new DoctorRating(mock(Caregiver.class), mock(Pacilian.class), 5, "Excellent");

        repository.save(rating1);
        repository.save(rating2);

        List<DoctorRating> allRatings = repository.findAll();
        assertEquals(2, allRatings.size());
        assertTrue(allRatings.contains(rating1));
        assertTrue(allRatings.contains(rating2));
    }

    @Test
    public void testDeleteByIdRemovesRating() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 3, "Decent");
        repository.save(rating);
        assertTrue(repository.existsById(rating.getId()));

        repository.deleteById(rating.getId());
        assertFalse(repository.existsById(rating.getId()));
    }

    @Test
    public void testExistsById() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 2, "Not great");
        repository.save(rating);

        assertTrue(repository.existsById(rating.getId()));
        assertFalse(repository.existsById("non-existent-id"));
    }

    @Test
    public void testSaveOverridesExistingRating() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 4, "Nice");
        repository.save(rating);

        rating.setValue(1);
        rating.setComment("Bad");
        repository.save(rating);

        DoctorRating result = repository.findById(rating.getId());
        assertEquals(1, result.getValue());
        assertEquals("Bad", result.getComment());
    }
}
