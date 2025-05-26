// package id.ac.ui.cs.advprog.b14.pandacare.rating.service;

// import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
// import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
// import id.ac.ui.cs.advprog.b14.pandacare.rating.repository.DoctorRatingRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.concurrent.CompletableFuture;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// public class DoctorRatingServiceTest {

//     private DoctorRatingRepository doctorRatingRepository;
//     private UserRepository userRepository;
//     private DoctorRatingService doctorRatingService;

//     private Caregiver caregiver;
//     private Pacilian pacilian;

//     @BeforeEach
//     public void setUp() {
//         doctorRatingRepository = mock(DoctorRatingRepository.class);
//         userRepository = mock(UserRepository.class);
//         doctorRatingService = new DoctorRatingService(doctorRatingRepository, userRepository);

//         caregiver = mock(Caregiver.class);
//         when(caregiver.getId()).thenReturn("care-1");

//         pacilian = mock(Pacilian.class);
//         when(pacilian.getId()).thenReturn("paci-1");

//         when(userRepository.findById("care-1")).thenReturn(Optional.of(caregiver));
//         when(userRepository.findById("paci-1")).thenReturn(Optional.of(pacilian));
//     }

//     @Test
//     public void testCreateRating() {
//         when(doctorRatingRepository.save(any(DoctorRating.class)))
//                 .thenAnswer(invocation -> invocation.getArgument(0));

//         DoctorRating created = doctorRatingService.createRating("care-1", "paci-1", 5, "Great doctor!");
//         assertNotNull(created.getId());
//         assertEquals(caregiver, created.getCaregiver());
//         assertEquals(pacilian, created.getPacilian());
//         assertEquals(5, created.getValue());
//         assertEquals("Great doctor!", created.getComment());
//         verify(doctorRatingRepository).save(any(DoctorRating.class));
//     }

//     @Test
//     public void testGetRatingById() {
//         DoctorRating rating = new DoctorRating(caregiver, pacilian, 4, "Nice");
//         when(doctorRatingRepository.findById(rating.getId())).thenReturn(rating);

//         DoctorRating result = doctorRatingService.getRatingById(rating.getId());
//         assertEquals(caregiver, result.getCaregiver());
//         assertEquals("Nice", result.getComment());
//     }

//     @Test
//     public void testGetAllRatings() {
//         List<DoctorRating> ratings = Arrays.asList(
//                 new DoctorRating(caregiver, pacilian, 4, "Good"),
//                 new DoctorRating(mock(Caregiver.class), mock(Pacilian.class), 5, "Excellent")
//         );
//         when(doctorRatingRepository.findAll()).thenReturn(ratings);

//         List<DoctorRating> result = doctorRatingService.getAllRatings();
//         assertEquals(2, result.size());
//     }

//     @Test
//     public void testUpdateRating() {
//         DoctorRating existing = new DoctorRating(caregiver, pacilian, 3, "Okay");
//         String id = existing.getId();

//         when(doctorRatingRepository.findById(id)).thenReturn(existing);
//         when(doctorRatingRepository.save(any(DoctorRating.class)))
//                 .thenAnswer(invocation -> invocation.getArgument(0));

//         DoctorRating updated = doctorRatingService.updateRating(id, 5, "Much better");

//         assertEquals(5, updated.getValue());
//         assertEquals("Much better", updated.getComment());
//         verify(doctorRatingRepository).save(any(DoctorRating.class));
//     }

//     @Test
//     public void testDeleteRating() {
//         String id = "some-id";

//         when(doctorRatingRepository.existsById(id)).thenReturn(true);

//         doctorRatingService.deleteRating(id);
//         verify(doctorRatingRepository, times(1)).deleteById(id);
//     }

//     @Test
//     void shouldReturnRatingsByDoctorAsync() throws Exception {
//         DoctorRating r = new DoctorRating(caregiver, pacilian, 5, "Great");
//         when(userRepository.findById("care-1")).thenReturn(Optional.of(caregiver));
//         when(doctorRatingRepository.findByCaregiver(caregiver)).thenReturn(List.of(r));

//         CompletableFuture<List<DoctorRating>> future =
//                 doctorRatingService.findByDoctorId("care-1");

//         assertEquals(1, future.get().size());
//         assertEquals(r, future.get().get(0));
//     }
// }
