package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.controller.AuthController;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.facade.AuthenticationFacade;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.TokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {AuthController.class, AccountController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenRepository tokenRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AuthenticationFacade authFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private UserProfileDTO userProfile1, userProfile2;
    private Pacilian testAccount1, testAccount2;
    private Map<String, Object> successResponse;
    private Map<String, Object> failureResponse;
    private Token token;

    @BeforeEach
    void setUp() {
        List<String> medicalHistory = new ArrayList<>();
        medicalHistory.add(UUID.randomUUID().toString());
        medicalHistory.add(UUID.randomUUID().toString());
        testAccount1 = new Pacilian(
                UUID.randomUUID().toString(),
                "testAccount1@gmail.com",
                "password123!",
                "TestAcc1",
                "1234567890",
                "Cisauk",
                "1234567890",
                medicalHistory
        );

        userProfile1 = UserProfileDTO.builder().build();
        userProfile1.setId(testAccount1.getId());
        userProfile1.setNik(testAccount1.getNik());
        userProfile1.setEmail(testAccount1.getEmail());
        userProfile1.setType(testAccount1.getType());
        userProfile1.setName(testAccount1.getName());
        userProfile1.setAddress(testAccount1.getAddress());
        userProfile1.setPhone(testAccount1.getPhone());
        userProfile1.setMedicalHistory(testAccount1.getMedicalHistory());

        testAccount2 = new Pacilian(
                testAccount1.getId(),
                "testAccount2@gmail.com",
                "password123!",
                "TestAcc2",
                "1234567890",
                "Cisauk",
                "1234567890",
                medicalHistory
        );

        userProfile2 = UserProfileDTO.builder().build();
        userProfile2.setId(testAccount2.getId());
        userProfile2.setNik(testAccount2.getNik());
        userProfile2.setEmail(testAccount2.getEmail());
        userProfile2.setType(testAccount2.getType());
        userProfile2.setName(testAccount2.getName());
        userProfile2.setAddress(testAccount2.getAddress());
        userProfile2.setPhone(testAccount2.getPhone());
        userProfile2.setMedicalHistory(testAccount2.getMedicalHistory());

        RegisterRequest request1 = new RegisterRequest();
        request1.setEmail(testAccount1.getEmail());
        request1.setPassword(testAccount1.getPassword());
        request1.setName(testAccount1.getName());
        request1.setPhone(testAccount1.getPhone());
        request1.setMedicalHistory(medicalHistory);


        RegisterRequest request2 = new RegisterRequest();
        request2.setEmail(testAccount2.getEmail());
        request2.setPassword(testAccount2.getPassword());
        request2.setName(testAccount2.getName());
        request2.setPhone(testAccount2.getPhone());
        request2.setMedicalHistory(medicalHistory);

        authFacade.registerPacilian(request1);
        authFacade.registerPacilian(request2);

        when(jwtUtil.generateToken(any(), any(), any())).thenReturn("fakeTokenString123");
        when(jwtUtil.getExpirationDateFromToken(any())).thenReturn(new Date(System.currentTimeMillis() + 100000000));

        String tokenString = jwtUtil.generateToken(testAccount1.getEmail(), testAccount1.getRole(), testAccount1.getId());
        Date expiryDate = jwtUtil.getExpirationDateFromToken(tokenString);
        tokenRepository.deleteExpiredTokens(new Date());

        token = new Token(tokenString, expiryDate);

        when(tokenRepository.findByToken("fakeTokenString123")).thenReturn(token);
        when(jwtUtil.validateToken(any())).thenReturn(true);

        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        tokenRepository.save(token);

        successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("message", "Success");
        Map<String, Object> data = new HashMap<>();
        data.put("token", tokenString);
        data.put("expiryDate", expiryDate.getTime());
        successResponse.put("data", data);

        failureResponse = new HashMap<>();
        failureResponse.put("success", false);
        failureResponse.put("message", "Error");

        when(authFacade.login(any())).thenReturn(new ResponseEntity<>(successResponse, HttpStatus.OK));

        SecurityContextHolder.clearContext();
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + testAccount1.getRole().toUpperCase())
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                testAccount1,
                null,
                authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUserProfile_returnsFound() throws Exception {
        when(accountService.getProfileById(testAccount1.getId())).thenReturn(userProfile1);
        when(accountService.findUserByEmail(testAccount1.getEmail())).thenReturn(testAccount1);

        mockMvc.perform(get("/api/profile/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccount1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(testAccount1.getId()))
                .andExpect(jsonPath("$.result.name").value(testAccount1.getName()));
    }

    @Test
    void testGetCurrentUserProfile_returnsNotFound() throws Exception {
        when(accountService.getProfileById(any())).thenReturn(null);

        mockMvc.perform(get("/api/profile/me")
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProfile_returnsSuccess() throws Exception {
        UpdateProfileDTO updateProfileDTO = UpdateProfileDTO.builder().build();
        updateProfileDTO.setType(userProfile2.getType());
        updateProfileDTO.setNik(userProfile2.getNik());
        updateProfileDTO.setEmail(userProfile2.getEmail());
        updateProfileDTO.setName(userProfile2.getName());
        updateProfileDTO.setPhone(userProfile2.getPhone());
        updateProfileDTO.setMedicalHistory(userProfile2.getMedicalHistory());

        when(accountService.updateProfile(any(), any())).thenReturn(userProfile2);

        mockMvc.perform(put("/api/profile/" + testAccount1.getId())
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(testAccount2.getId()))
                .andExpect(jsonPath("$.result.name").value(testAccount2.getName()));
    }

    @Test
    void testDeleteUser_returnsFound() throws Exception {
        mockMvc.perform(delete("/api/profile/" + testAccount1.getId())
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserByEmail_returnsFound() throws Exception {
        when(accountService.findUserByEmail(testAccount1.getEmail())).thenReturn(testAccount1);

        mockMvc.perform(get("/api/profile/search/findByEmail?email=" + testAccount1.getEmail())
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testAccount1.getId()));
    }

    @Test
    void testGetUserByEmail_returnsNotFound() throws Exception {
        when(accountService.findUserByEmail(any())).thenReturn(null);

        mockMvc.perform(get("/api/profile/search/findByEmail?email=test@test.com")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isNotFound());
    }


}
