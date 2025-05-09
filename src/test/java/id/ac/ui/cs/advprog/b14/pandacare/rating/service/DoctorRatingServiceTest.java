package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorRatingServiceTest {

    private DoctorRatingRepository doctorRatingRepository;
    private DoctorRatingService doctorRatingService;

    @BeforeEach
    public void setUp() {
        doctorRatingRepository = mock(DoctorRatingRepository.class);
        doctorRatingService = new DoctorRatingService(doctorRatingRepository);
    }

    @Test
    public void testCreateRating() {
        DoctorRating created = doctorRatingService.createRating("care-1", "paci-1", 5, "Great doctor!");
        assertNotNull(created.getId());
        assertEquals("care-1", created.getCaregiverId());
        assertEquals("paci-1", created.getPacilianId());
        assertEquals(5, created.getValue());
        assertEquals("Great doctor!", created.getComment());
        verify(doctorRatingRepository).save(any(DoctorRating.class));
    }

    @Test
    public void testGetRatingById() {
        DoctorRating rating = new DoctorRating("care-1", "paci-1", 4, "Nice");
        when(doctorRatingRepository.findById(rating.getId())).thenReturn(rating);

        DoctorRating result = doctorRatingService.getRatingById(rating.getId());
        assertEquals("care-1", result.getCaregiverId());
        assertEquals("Nice", result.getComment());
    }

    @Test
    public void testGetAllRatings() {
        List<DoctorRating> ratings = Arrays.asList(
                new DoctorRating("care-1", "paci-1", 4, "Good"),
                new DoctorRating("care-2", "paci-2", 5, "Excellent")
        );
        when(doctorRatingRepository.findAll()).thenReturn(ratings);

        List<DoctorRating> result = doctorRatingService.getAllRatings();
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateRating() {
        DoctorRating existing = new DoctorRating("care-1", "paci-1", 3, "Okay");
        String id = existing.getId();
        when(doctorRatingRepository.findById(id)).thenReturn(existing);

        DoctorRating updated = doctorRatingService.updateRating(id, 5, "Much better");

        assertEquals(5, updated.getValue());
        assertEquals("Much better", updated.getComment());
        verify(doctorRatingRepository).save(existing);
    }

    @Test
    public void testDeleteRating() {
        String id = "some-id";
        doctorRatingService.deleteRating(id);
        verify(doctorRatingRepository, times(1)).deleteById(id);
    }
}
