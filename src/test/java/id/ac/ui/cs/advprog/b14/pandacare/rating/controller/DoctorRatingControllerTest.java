// package id.ac.ui.cs.advprog.b14.pandacare.rating.controller;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtAuthFilter;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
// import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
// import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.RatingRequest;
// import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
// import id.ac.ui.cs.advprog.b14.pandacare.rating.service.DoctorRatingService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;

// import java.util.List;
// import java.util.UUID;
// import java.util.concurrent.CompletableFuture;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import static org.mockito.Mockito.mock;

// @WebMvcTest(controllers = DoctorRatingController.class)
// @AutoConfigureMockMvc(addFilters = false)
// public class DoctorRatingControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockitoBean
//     private DoctorRatingService doctorRatingService;

//     @MockitoBean
//     private JwtUtil jwtUtil;

//     @MockitoBean
//     private JwtAuthFilter jwtAuthFilter;

//     @Autowired
//     private ObjectMapper objectMapper;

//     private DoctorRating rating;
//     private RatingRequest request;
//     private Caregiver caregiver;
//     private Pacilian pacilian;

//     @BeforeEach
//     void setUp() {
//         // Mock caregiver
//         caregiver = mock(Caregiver.class);
//         when(caregiver.getId()).thenReturn("care-1");
//         when(caregiver.getName()).thenReturn("Dr. Care");

//         // Mock pacilian
//         pacilian = mock(Pacilian.class);
//         when(pacilian.getId()).thenReturn("paci-1");
//         when(pacilian.getName()).thenReturn("Patient One");

//         // Rating instance
//         rating = new DoctorRating();
//         rating.setId(UUID.randomUUID().toString());
//         rating.setCaregiver(caregiver);
//         rating.setPacilian(pacilian);
//         rating.setValue(5);
//         rating.setComment("Great service");

//         // DTO request
//         request = new RatingRequest();
//         request.setCaregiverId("care-1");
//         request.setPacilianId("paci-1");
//         request.setValue(5);
//         request.setComment("Great service");
//     }

//     @Test
//     void testCreateRating() throws Exception {
//         when(doctorRatingService.createRating(anyString(), anyString(), anyInt(), anyString()))
//                 .thenReturn(rating);

//         mockMvc.perform(post("/api/ratings")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.success").value(true))
//                 .andExpect(jsonPath("$.message").value("Rating created successfully"))
//                 .andExpect(jsonPath("$.data.value").value(5));
//     }

//     @Test
//     void testGetRatingById() throws Exception {
//         when(doctorRatingService.getRatingById(anyString())).thenReturn(rating);

//         mockMvc.perform(get("/api/ratings/{id}", "dummy-id"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.comment").value("Great service"))
//                 .andExpect(jsonPath("$.data.caregiverId").value("care-1"));
//     }

//     @Test
//     void testUpdateRating() throws Exception {
//         rating.setValue(4);
//         rating.setComment("Updated comment");

//         when(doctorRatingService.updateRating(anyString(), anyInt(), anyString()))
//                 .thenReturn(rating);

//         request.setValue(4);
//         request.setComment("Updated comment");

//         mockMvc.perform(put("/api/ratings/{id}", "dummy-id")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(request)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data.value").value(4))
//                 .andExpect(jsonPath("$.data.comment").value("Updated comment"));
//     }

//     @Test
//     void testDeleteRating() throws Exception {
//         mockMvc.perform(delete("/api/ratings/{id}", "dummy-id"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.success").value(true))
//                 .andExpect(jsonPath("$.message").value("Rating deleted successfully"));
//     }

//     @Test
//     void testGetAllRatings() throws Exception {
//         when(doctorRatingService.getAllRatings()).thenReturn(List.of(rating));

//         mockMvc.perform(get("/api/ratings"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data[0].value").value(5))
//                 .andExpect(jsonPath("$.data[0].caregiverName").value("Dr. Care"));
//     }

//     @Test
//     void testGetByDoctorIdAsync() throws Exception {
//         when(doctorRatingService.findByDoctorId("care-1"))
//                 .thenReturn(CompletableFuture.completedFuture(List.of(rating)));

//         var mvcResult = mockMvc.perform(get("/api/ratings/doctor/{doctorId}", "care-1"))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.data[0].value").value(5))
//                 .andExpect(jsonPath("$.data[0].pacilianName").value("Patient One"));
//     }
// }
