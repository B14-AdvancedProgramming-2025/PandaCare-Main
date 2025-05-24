package id.ac.ui.cs.advprog.b14.pandacare.authentication.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.TokenRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private Pacilian mockPacilian;
    private Caregiver mockCaregiver;
    private Token mockToken;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private String validToken;
    private Date expiryDate;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);

        mockPacilian = new Pacilian(
                "test-id",
                "test@example.com",
                "encodedPassword",
                "Test User",
                "1234567890123456",
                "Test Address",
                "08123456789",
                Arrays.asList("Asthma")
        );
        mockPacilian.setType(UserType.PACILIAN);

        mockCaregiver = new Caregiver(
                "test-id",
                "caregiver@example.com",
                "encodedPassword",
                "Caregiver User",
                "1234567890123456",
                "Caregiver Address",
                "08123456789",
                "Elderly Care",
                Arrays.asList("Monday 9-5")
        );
        mockCaregiver.setType(UserType.CAREGIVER);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("New User");
        registerRequest.setNik("1234567890123456");
        registerRequest.setAddress("New Address");
        registerRequest.setPhone("08123456789");
        registerRequest.setMedicalHistory(Arrays.asList("None"));
        registerRequest.setSpecialty("Elderly Care");
        registerRequest.setWorkingSchedule(Arrays.asList("Monday 9-5"));

        validToken = "valid.jwt.token";
        expiryDate = new Date(System.currentTimeMillis() + 3600000);
        mockToken = new Token(validToken, expiryDate);
    }

    @Test
    void loginSuccessful() {
        when(mockUser.getEmail()).thenReturn("test@example.com");
        when(mockUser.getPassword()).thenReturn("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn(validToken);
        when(jwtUtil.getExpirationDateFromToken(validToken)).thenReturn(expiryDate);

        ResponseEntity<Map<String, Object>> response = authService.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("Login successful", responseBody.get("message"));

        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        assertEquals(validToken, data.get("token"));
        assertEquals(expiryDate.getTime(), data.get("expiryDate"));

        verify(tokenRepository).deleteExpiredTokens(any(Date.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void loginUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = authService.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("User not found", responseBody.get("message"));
    }

    @Test
    void loginInvalidCredentials() {
        when(mockUser.getPassword()).thenReturn("encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = authService.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Invalid credentials", responseBody.get("message"));
    }

    @Test
    void registerPacilianSuccessful() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(Pacilian.class))).thenReturn(mockPacilian);

        ResponseEntity<Map<String, Object>> response = authService.registerPacilian(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("Registration successful", responseBody.get("message"));

        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        assertEquals("test@example.com", data.get("email"));
        assertEquals("Test User", data.get("name"));
    }

    @Test
    void registerPacilianEmailAlreadyExists() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(mockUser);

        ResponseEntity<Map<String, Object>> response = authService.registerPacilian(registerRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Email already registered", responseBody.get("message"));
    }

    @Test
    void registerCaregiverSuccessful() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(Caregiver.class))).thenReturn(mockCaregiver);

        ResponseEntity<Map<String, Object>> response = authService.registerCaregiver(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("Registration successful", responseBody.get("message"));

        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        assertEquals("caregiver@example.com", data.get("email"));
        assertEquals("Caregiver User", data.get("name"));
    }

    @Test
    void registerCaregiverEmailAlreadyExists() {
        when(userRepository.findByEmail("new@example.com")).thenReturn(mockUser);

        ResponseEntity<Map<String, Object>> response = authService.registerCaregiver(registerRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Email already registered", responseBody.get("message"));
    }

    @Test
    void logoutSuccessful() {
        String authHeader = "Bearer " + validToken;
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(tokenRepository.findByToken(validToken)).thenReturn(mockToken);

        ResponseEntity<Map<String, Object>> response = authService.logout(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("Logout successful", responseBody.get("message"));

        verify(tokenRepository).deleteByToken(validToken);
    }

    @Test
    void logoutInvalidTokenFormat() {
        String authHeader = "Invalid " + validToken;

        ResponseEntity<Map<String, Object>> response = authService.logout(authHeader);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Invalid token", responseBody.get("message"));
    }

    @Test
    void logoutTokenNotFound() {
        String authHeader = "Bearer " + validToken;
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(tokenRepository.findByToken(validToken)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = authService.logout(authHeader);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Invalid token", responseBody.get("message"));
    }

    @Test
    void refreshTokenSuccessful() {
        String authHeader = "Bearer " + validToken;
        String newToken = "new.jwt.token";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(tokenRepository.findByToken(validToken)).thenReturn(mockToken);
        when(jwtUtil.getEmailFromToken(validToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(jwtUtil.generateToken("test@example.com")).thenReturn(newToken);
        when(jwtUtil.getExpirationDateFromToken(newToken)).thenReturn(expiryDate);

        ResponseEntity<Map<String, Object>> response = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals("Token refreshed successfully", responseBody.get("message"));

        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        assertEquals(newToken, data.get("token"));
        assertEquals(expiryDate.getTime(), data.get("expiryDate"));

        verify(tokenRepository).deleteByToken(validToken);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void refreshTokenInvalidToken() {
        String authHeader = "Bearer " + validToken;
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("Invalid or expired token", responseBody.get("message"));
    }

    @Test
    void refreshTokenUserNotFound() {
        String authHeader = "Bearer " + validToken;

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(tokenRepository.findByToken(validToken)).thenReturn(mockToken);
        when(jwtUtil.getEmailFromToken(validToken)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = authService.refreshToken(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertFalse((Boolean) responseBody.get("success"));
        assertEquals("User not found", responseBody.get("message"));
    }
}