package id.ac.ui.cs.advprog.b14.pandacare.authentication.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("valid@example.com");
        loginRequest.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmailFormat() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid-email");
        loginRequest.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty());

        boolean hasNotBlankViolation = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Email cannot be empty"));
        assertTrue(hasNotBlankViolation);
    }

    @Test
    void testNullEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(null);
        loginRequest.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("valid@example.com");
        loginRequest.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("valid@example.com");
        loginRequest.setPassword(null);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testGettersAndSetters() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("testPassword");

        assertEquals("test@example.com", loginRequest.getEmail());
        assertEquals("testPassword", loginRequest.getPassword());
    }
}