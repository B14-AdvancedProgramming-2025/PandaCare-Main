package id.ac.ui.cs.advprog.b14.pandacare.rating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b14.pandacare.rating.model.DoctorRating;
import id.ac.ui.cs.advprog.b14.pandacare.rating.service.DoctorRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorRatingController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security for test
public class DoctorRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorRatingService doctorRatingService; // ✅ fix from @MockitoBean to @MockBean

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorRating rating;

    @BeforeEach
    public void setUp() {
        rating = new DoctorRating("caregiver-123", "pacilian-456", 5, "Excellent!");
    }

    @Test
    public void testCreateRating() throws Exception {
        when(doctorRatingService.createRating(anyString(), anyString(), anyInt(), anyString()))
                .thenReturn(rating);

        mockMvc.perform(post("/api/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caregiverId").value("caregiver-123"))
                .andExpect(jsonPath("$.pacilianId").value("pacilian-456"))
                .andExpect(jsonPath("$.value").value(5))
                .andExpect(jsonPath("$.comment").value("Excellent!"));
    }

    @Test
    public void testGetAllRatings() throws Exception {
        List<DoctorRating> ratings = List.of(rating);
        when(doctorRatingService.getAllRatings()).thenReturn(ratings);

        mockMvc.perform(get("/api/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetRatingById() throws Exception {
        String id = rating.getId();
        when(doctorRatingService.getRatingById(id)).thenReturn(rating);

        mockMvc.perform(get("/api/ratings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Excellent!"));
    }

    @Test
    public void testUpdateRating() throws Exception {
        String id = UUID.randomUUID().toString();
        DoctorRating updated = new DoctorRating("caregiver-123", "pacilian-456", 4, "Good!");
        when(doctorRatingService.updateRating(eq(id), anyInt(), anyString()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/ratings/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(4))
                .andExpect(jsonPath("$.comment").value("Good!"));
    }

    @Test
    public void testDeleteRating() throws Exception {
        String id = UUID.randomUUID().toString();
        doNothing().when(doctorRatingService).deleteRating(id);

        mockMvc.perform(delete("/api/ratings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Rating deleted successfully."));
    }
}
