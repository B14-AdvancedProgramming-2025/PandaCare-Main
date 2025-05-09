package id.ac.ui.cs.advprog.b14.pandacare.rating.repository;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorRatingRepositoryTest {

    private DoctorRatingRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new DoctorRatingRepository();
    }

    @Test
    public void testSaveAndFindById() {
        DoctorRating rating = new DoctorRating("caregiver-1", "pacilian-1", 5, "Good service");
        repository.save(rating);

        DoctorRating result = repository.findById(rating.getId());
        assertNotNull(result);
        assertEquals("caregiver-1", result.getCaregiverId());
        assertEquals("pacilian-1", result.getPacilianId());
        assertEquals(5, result.getValue());
        assertEquals("Good service", result.getComment());
    }

    @Test
    public void testFindAllReturnsAllRatings() {
        DoctorRating rating1 = new DoctorRating("caregiver-A", "pacilian-A", 4, "Okay");
        DoctorRating rating2 = new DoctorRating("caregiver-B", "pacilian-B", 5, "Excellent");

        repository.save(rating1);
        repository.save(rating2);

        List<DoctorRating> allRatings = repository.findAll();
        assertEquals(2, allRatings.size());
        assertTrue(allRatings.contains(rating1));
        assertTrue(allRatings.contains(rating2));
    }

    @Test
    public void testDeleteByIdRemovesRating() {
        DoctorRating rating = new DoctorRating("caregiver-2", "pacilian-2", 3, "Decent");
        repository.save(rating);
        assertTrue(repository.existsById(rating.getId()));

        repository.deleteById(rating.getId());
        assertFalse(repository.existsById(rating.getId()));
    }

    @Test
    public void testExistsById() {
        DoctorRating rating = new DoctorRating("caregiver-3", "pacilian-3", 2, "Not great");
        repository.save(rating);

        assertTrue(repository.existsById(rating.getId()));
        assertFalse(repository.existsById("non-existent-id"));
    }

    @Test
    public void testSaveOverridesExistingRating() {
        DoctorRating rating = new DoctorRating("caregiver-4", "pacilian-4", 4, "Nice");
        repository.save(rating);

        rating.setValue(1);
        rating.setComment("Bad");
        repository.save(rating);

        DoctorRating result = repository.findById(rating.getId());
        assertEquals(1, result.getValue());
        assertEquals("Bad", result.getComment());
    }
}
