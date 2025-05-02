package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class DoctorRatingServiceTest {

    private DoctorRatingRepository doctorRatingRepository;
    private DoctorRatingService doctorRatingService;

    @BeforeEach
    public void setUp() {
        doctorRatingRepository = Mockito.mock(DoctorRatingRepository.class);
        doctorRatingService = new DoctorRatingService(doctorRatingRepository);
    }

    @Test
    public void testSaveRating() {
        DoctorRating rating = new DoctorRating();
        doctorRatingService.saveRating(rating);
        verify(doctorRatingRepository, times(1)).save(rating);
    }
}