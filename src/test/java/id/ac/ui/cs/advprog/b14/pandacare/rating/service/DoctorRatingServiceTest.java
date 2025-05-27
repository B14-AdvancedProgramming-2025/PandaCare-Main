package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DoctorRatingServiceTest {

    private DoctorRatingRepository doctorRatingRepository;
    private UserRepository userRepository;
    private DoctorRatingService doctorRatingService;

    private Caregiver caregiver;
    private Pacilian pacilian;

    @BeforeEach
    public void setUp() {
        doctorRatingRepository = mock(DoctorRatingRepository.class);
        userRepository = mock(UserRepository.class);
        doctorRatingService = new DoctorRatingService(doctorRatingRepository, userRepository);

        // mock Caregiver
        caregiver = mock(Caregiver.class);
        when(caregiver.getId()).thenReturn("care-1");
        when(userRepository.findById("care-1"))
                .thenReturn(Optional.of((id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User) caregiver));

        // mock Pacilian
        pacilian = mock(Pacilian.class);
        when(pacilian.getId()).thenReturn("paci-1");
        when(userRepository.findByEmail("paci-1")).thenReturn(pacilian);
        when(userRepository.findById("paci-1"))
                .thenReturn(Optional.of((id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User) pacilian));

        // default save behavior
        when(doctorRatingRepository.save(any(DoctorRating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testCreateRating_Success() {
        DoctorRating created = doctorRatingService.createRating(
                "care-1", "paci-1", 5, "Great doctor!"
        );

        assertNotNull(created.getId());
        assertSame(caregiver, created.getCaregiver());
        assertSame(pacilian, created.getPacilian());
        assertEquals(5, created.getValue());
        assertEquals("Great doctor!", created.getComment());
        verify(doctorRatingRepository, times(1)).save(any(DoctorRating.class));
    }

    @Test
    public void testCreateRating_PacilianNotFound() {
        when(userRepository.findByEmail("unknown")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> doctorRatingService.createRating("care-1", "unknown", 4, "Bad")
        );
        assertTrue(ex.getMessage().contains("Pacilian dengan email unknown tidak ditemukan"));
    }

    @Test
    public void testGetRatingById() {
        DoctorRating rating = new DoctorRating(caregiver, pacilian, 4, "Nice");
        when(doctorRatingRepository.findById(rating.getId()))
                .thenReturn(Optional.of(rating));

        DoctorRating result = doctorRatingService.getRatingById(rating.getId());
        assertSame(caregiver, result.getCaregiver());
        assertEquals("Nice", result.getComment());
    }

    @Test
    public void testGetAllRatings() {
        DoctorRating r1 = new DoctorRating(caregiver, pacilian, 4, "Good");
        DoctorRating r2 = new DoctorRating(mock(Caregiver.class), mock(Pacilian.class), 5, "Excellent");
        when(doctorRatingRepository.findAll())
                .thenReturn(Arrays.asList(r1, r2));

        List<DoctorRating> result = doctorRatingService.getAllRatings();
        assertEquals(2, result.size());
        assertEquals("Good", result.get(0).getComment());
        assertEquals("Excellent", result.get(1).getComment());
    }

    @Test
    public void testUpdateRating() {
        DoctorRating existing = new DoctorRating(caregiver, pacilian, 3, "Okay");
        String id = existing.getId();
        when(doctorRatingRepository.findById(id))
                .thenReturn(Optional.of(existing));
        when(doctorRatingRepository.save(any(DoctorRating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DoctorRating updated = doctorRatingService.updateRating(id, 5, "Much better");
        assertEquals(5, updated.getValue());
        assertEquals("Much better", updated.getComment());
        verify(doctorRatingRepository, times(1)).save(existing);
    }

    @Test
    public void testDeleteRating() {
        String id = "some-id";
        when(doctorRatingRepository.existsById(id)).thenReturn(true);

        doctorRatingService.deleteRating(id);
        verify(doctorRatingRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnRatingsByDoctorAsync() throws Exception {
        DoctorRating r = new DoctorRating(caregiver, pacilian, 5, "Great");
        when(doctorRatingRepository.findByCaregiver_Id("care-1"))
                .thenReturn(List.of(r));

        CompletableFuture<List<DoctorRating>> future = doctorRatingService.findByDoctorId("care-1");
        List<DoctorRating> list = future.get();
        assertEquals(1, list.size());
        assertSame(r, list.get(0));
    }
}
