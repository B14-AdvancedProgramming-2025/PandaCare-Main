package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AccountController.class, AccountControllerTest.TestConfig.class})
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    private Pacilian testPacilian;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testPacilian = Pacilian.builder()
                .id(UUID.randomUUID().toString())
                .email("test@example.com")
                .password("password")
                .name("John Doe")
                .nik("1234567890")
                .address("Somewhere Street")
                .phone("08123456789")
                .medicalHistory(List.of("Asthma", "Diabetes"))
                .build();

        Mockito.reset(accountService);
    }

    @Test
    void testGetProfileById() throws Exception {
        when(accountService.getProfileById(testPacilian.getId())).thenReturn(testPacilian);

        mockMvc.perform(get("/api/profile/" + testPacilian.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200)) // was $.status
                .andExpect(jsonPath("$.message").value("Profile retrieved successfully"))
                .andExpect(jsonPath("$.result.name").value("John Doe"))
                .andExpect(jsonPath("$.result.medicalHistory[0]").value("Asthma"));
    }

    @Test
    void testGetProfileByIdNotFound() throws Exception {
        UUID fakeId = UUID.randomUUID();
        when(accountService.getProfileById(fakeId.toString()))
                .thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(get("/api/profile/" + fakeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        when(accountService.updateProfile(eq(testPacilian.getId()), any(User.class))).thenReturn(testPacilian);

        mockMvc.perform(put("/api/profile/" + testPacilian.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPacilian)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Profile updated successfully"))
                .andExpect(jsonPath("$.result.name").value("John Doe"))
                .andExpect(jsonPath("$.result.medicalHistory[1]").value("Diabetes"));
    }

    @Test
    void testUpdateProfileNotFound() throws Exception {
        UUID fakeId = UUID.randomUUID();
        when(accountService.updateProfile(eq(fakeId.toString()), any(User.class)))
                .thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(put("/api/profile/" + fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPacilian)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testDeleteProfile() throws Exception {
        doNothing().when(accountService).deleteProfile(testPacilian.getId());

        mockMvc.perform(delete("/api/profile/" + testPacilian.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Profile deleted successfully"))
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    void testDeleteProfileNotFound() throws Exception {
        UUID fakeId = UUID.randomUUID();
        doThrow(new NoSuchElementException("User not found"))
                .when(accountService).deleteProfile(fakeId.toString());

        mockMvc.perform(delete("/api/profile/" + fakeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }
    }
}
