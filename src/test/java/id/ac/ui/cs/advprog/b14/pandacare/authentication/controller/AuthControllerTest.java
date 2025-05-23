package id.ac.ui.cs.advprog.b14.pandacare.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.facade.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @MockBean
    private AuthenticationFacade authFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private Map<String, Object> successResponse;
    private Map<String, Object> failureResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("password");
        registerRequest.setName("Test User");
        registerRequest.setNik("1234567890123456");
        registerRequest.setAddress("Test Address");
        registerRequest.setPhone("08123456789");
        registerRequest.setMedicalHistory(Arrays.asList("None"));
        registerRequest.setSpecialty("Elderly Care");
        registerRequest.setWorkingSchedule(Arrays.asList("Monday 9-5"));

        successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("message", "Success");
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", "test@example.com");
        userData.put("name", "Test User");
        successResponse.put("data", userData);

        failureResponse = new HashMap<>();
        failureResponse.put("success", false);
        failureResponse.put("message", "Error");
    }

    @Test
    void login_Success() throws Exception {
        when(authFacade.login(any(LoginRequest.class)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(successResponse)));

        verify(authFacade).login(any(LoginRequest.class));
    }

    @Test
    void registerPacilian_Success() throws Exception {
        when(authFacade.registerPacilian(any(RegisterRequest.class)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.CREATED));

        mockMvc.perform(post("/api/auth/register/pacilian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(successResponse)));

        verify(authFacade).registerPacilian(any(RegisterRequest.class));
    }

    @Test
    void registerCaregiver_Success() throws Exception {
        when(authFacade.registerCaregiver(any(RegisterRequest.class)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.CREATED));

        mockMvc.perform(post("/api/auth/register/caregiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(successResponse)));

        verify(authFacade).registerCaregiver(any(RegisterRequest.class));
    }

    @Test
    void logout_Success() throws Exception {
        String authHeader = "Bearer valid.token";
        when(authFacade.logout(eq(authHeader)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(successResponse)));

        verify(authFacade).logout(eq(authHeader));
    }

    @Test
    void refreshToken_Success() throws Exception {
        String authHeader = "Bearer valid.token";
        when(authFacade.refreshToken(eq(authHeader)))
                .thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));

        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(successResponse)));

        verify(authFacade).refreshToken(eq(authHeader));
    }

    @Test
    void login_BadRequest() throws Exception {
        // Empty login request to trigger validation errors
        LoginRequest invalidRequest = new LoginRequest();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerPacilian_BadRequest() throws Exception {
        // Empty register request to trigger validation errors
        RegisterRequest invalidRequest = new RegisterRequest();

        mockMvc.perform(post("/api/auth/register/pacilian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void logout_Unauthorized() throws Exception {
        String authHeader = "Bearer invalid.token";
        when(authFacade.logout(eq(authHeader)))
                .thenReturn(new ResponseEntity<>(failureResponse, HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", authHeader))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(objectMapper.writeValueAsString(failureResponse)));
    }

    @Test
    void refreshToken_Unauthorized() throws Exception {
        String authHeader = "Bearer invalid.token";
        when(authFacade.refreshToken(eq(authHeader)))
                .thenReturn(new ResponseEntity<>(failureResponse, HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", authHeader))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(objectMapper.writeValueAsString(failureResponse)));
    }
}