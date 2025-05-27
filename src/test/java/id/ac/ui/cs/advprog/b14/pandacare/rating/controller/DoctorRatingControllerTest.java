package id.ac.ui.cs.advprog.b14.pandacare.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtAuthFilter;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.DoctorRatingSummaryDto;
import id.ac.ui.cs.advprog.b14.pandacare.rating.dto.RatingRequest;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.service.DoctorRatingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorRatingController.class)
@AutoConfigureMockMvc(addFilters = false)  // nonaktifkan JwtAuthFilter
class DoctorRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private DoctorRatingService doctorRatingService;

    // Mock filter + util supaya bean JwtAuthFilter & JwtUtil tersedia
    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    private DoctorRating sampleRating(String id) {
        DoctorRating r = new DoctorRating();
        r.setId(id);

        // mock Caregiver
        Caregiver cg = mock(Caregiver.class);
        when(cg.getId()).thenReturn("doc-" + id);
        when(cg.getName()).thenReturn("Dr. " + id);
        r.setCaregiver(cg);

        // mock Pacilian
        Pacilian p = mock(Pacilian.class);
        when(p.getId()).thenReturn("paci-" + id);
        when(p.getName()).thenReturn("Pacilian " + id);
        r.setPacilian(p);

        r.setValue(4);
        r.setComment("Good");
        return r;
    }

    @Test
    @DisplayName("POST /api/ratings -> create rating and return 201")
    void createRating_ShouldReturnCreated() throws Exception {
        RatingRequest req = new RatingRequest();
        req.setCaregiverId("doc-1");
        req.setPacilianEmail("pac@example.com");
        req.setValue(5);
        req.setComment("Excellent");

        DoctorRating saved = sampleRating("1");
        given(doctorRatingService.createRating(
                eq("doc-1"), eq("pac@example.com"), eq(5), eq("Excellent")
        )).willReturn(saved);

        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is("1")))
                .andExpect(jsonPath("$.data.value", is(4)));
    }

    @Test
    @DisplayName("GET /api/ratings -> list all ratings")
    void getAllRatings_ShouldReturnList() throws Exception {
        List<DoctorRating> list = Arrays.asList(sampleRating("a"), sampleRating("b"));
        given(doctorRatingService.getAllRatings()).willReturn(list);

        mockMvc.perform(get("/api/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is("a")))
                .andExpect(jsonPath("$.data[1].id", is("b")));
    }

    @Test
    @DisplayName("GET /api/ratings/{id} -> existing rating")
    void getRatingById_Exists_ShouldReturnOk() throws Exception {
        DoctorRating r = sampleRating("42");
        given(doctorRatingService.getRatingById("42")).willReturn(r);

        mockMvc.perform(get("/api/ratings/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is("42")));
    }

    @Test
    @DisplayName("GET /api/ratings/{id} -> not found")
    void getRatingById_NotFound_ShouldReturn404() throws Exception {
        given(doctorRatingService.getRatingById("99"))
                .willThrow(new RuntimeException("Rating not found"));

        mockMvc.perform(get("/api/ratings/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Rating not found")));
    }

    @Test
    @DisplayName("PUT /api/ratings/{id} -> update rating")
    void updateRating_ShouldReturnOk() throws Exception {
        RatingRequest req = new RatingRequest();
        req.setValue(3);
        req.setComment("Average");

        DoctorRating updated = sampleRating("7");
        updated.setValue(3);
        updated.setComment("Average");
        given(doctorRatingService.updateRating(eq("7"), eq(3), eq("Average")))
                .willReturn(updated);

        mockMvc.perform(put("/api/ratings/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.value", is(3)));
    }

    @Test
    @DisplayName("DELETE /api/ratings/{id} -> delete rating")
    void deleteRating_ShouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/ratings/xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));
    }

    @Test
    @DisplayName("GET /api/ratings/doctor/{doctorId} -> list by doctor (async)")
    void getByDoctor_ShouldReturnOk() throws Exception {
        // 1) Stub service untuk mengembalikan CompletableFuture
        List<DoctorRating> list = Collections.singletonList(sampleRating("d1"));
        given(doctorRatingService.findByDoctorId("d1"))
                .willReturn(CompletableFuture.completedFuture(list));

        // 2) Lakukan perform dan expect async started
        MvcResult mvcResult = mockMvc.perform(get("/api/ratings/doctor/d1"))
                .andExpect(request().asyncStarted())
                .andReturn();

        // 3) Dispatch ulang setelah future selesai
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is("d1")));
    }

    @Test
    @DisplayName("GET /api/ratings/id/{email} -> existing user")
    void getIdByEmail_Exists_ShouldReturnOk() throws Exception {
        // Mock user
        User user = mock(User.class);
        when(user.getId()).thenReturn("user-123");
        given(userRepository.findByEmail("test@example.com")).willReturn(user);

        // Perform the request
        mockMvc.perform(get("/api/ratings/id/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", is("user-123")));
    }

    @Test
    @DisplayName("GET /api/ratings/id/{email} -> user not found")
    void getIdByEmail_NotFound_ShouldReturn404() throws Exception {
        // Mock user not found
        given(userRepository.findByEmail("unknown@example.com")).willThrow(new RuntimeException("User not found"));

        // Perform the request
        mockMvc.perform(get("/api/ratings/id/unknown@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("User not found")));
    }
}
