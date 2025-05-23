package id.ac.ui.cs.advprog.b14.pandacare.authentication.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private Validator validator;
    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validRequest = new RegisterRequest();
        validRequest.setEmail("valid@example.com");
        validRequest.setPassword("password123");
        validRequest.setName("John Doe");
        validRequest.setNik("1234567890123456");
        validRequest.setAddress("123 Main St");
        validRequest.setPhone("08123456789");
        validRequest.setMedicalHistory(Arrays.asList("Asthma", "Allergy"));
        validRequest.setSpecialty("Elderly Care");
        validRequest.setWorkingSchedule(Arrays.asList("Monday 9-5", "Wednesday 9-5"));
    }

    @Test
    void testValidRegisterRequest() {
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmailFormat() {
        RegisterRequest request = copyValidRequest();
        request.setEmail("invalid-email");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankEmail() {
        RegisterRequest request = copyValidRequest();
        request.setEmail("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        boolean hasNotBlankViolation = violations.stream()
                .anyMatch(v -> v.getMessage().equals("Email cannot be empty"));
        assertTrue(hasNotBlankViolation);
    }

    @Test
    void testNullEmail() {
        RegisterRequest request = copyValidRequest();
        request.setEmail(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Email cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankPassword() {
        RegisterRequest request = copyValidRequest();
        request.setPassword("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullPassword() {
        RegisterRequest request = copyValidRequest();
        request.setPassword(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Password cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankName() {
        RegisterRequest request = copyValidRequest();
        request.setName("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullName() {
        RegisterRequest request = copyValidRequest();
        request.setName(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Name cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankNik() {
        RegisterRequest request = copyValidRequest();
        request.setNik("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("NIK cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullNik() {
        RegisterRequest request = copyValidRequest();
        request.setNik(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("NIK cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankAddress() {
        RegisterRequest request = copyValidRequest();
        request.setAddress("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Address cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullAddress() {
        RegisterRequest request = copyValidRequest();
        request.setAddress(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Address cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankPhone() {
        RegisterRequest request = copyValidRequest();
        request.setPhone("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testNullPhone() {
        RegisterRequest request = copyValidRequest();
        request.setPhone(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone cannot be empty", violations.iterator().next().getMessage());
    }

    @Test
    void testOptionalFields() {
        RegisterRequest request = copyValidRequest();
        request.setMedicalHistory(null);
        request.setSpecialty(null);
        request.setWorkingSchedule(null);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        RegisterRequest request = new RegisterRequest();

        String email = "test@example.com";
        String password = "password123";
        String name = "Test User";
        String nik = "1234567890123456";
        String address = "Test Address";
        String phone = "08123456789";
        List<String> medicalHistory = Arrays.asList("Test History");
        String specialty = "Test Specialty";
        List<String> workingSchedule = Arrays.asList("Monday 9-5");

        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        request.setNik(nik);
        request.setAddress(address);
        request.setPhone(phone);
        request.setMedicalHistory(medicalHistory);
        request.setSpecialty(specialty);
        request.setWorkingSchedule(workingSchedule);

        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
        assertEquals(name, request.getName());
        assertEquals(nik, request.getNik());
        assertEquals(address, request.getAddress());
        assertEquals(phone, request.getPhone());
        assertEquals(medicalHistory, request.getMedicalHistory());
        assertEquals(specialty, request.getSpecialty());
        assertEquals(workingSchedule, request.getWorkingSchedule());
    }

    private RegisterRequest copyValidRequest() {
        RegisterRequest copy = new RegisterRequest();
        copy.setEmail(validRequest.getEmail());
        copy.setPassword(validRequest.getPassword());
        copy.setName(validRequest.getName());
        copy.setNik(validRequest.getNik());
        copy.setAddress(validRequest.getAddress());
        copy.setPhone(validRequest.getPhone());
        copy.setMedicalHistory(validRequest.getMedicalHistory());
        copy.setSpecialty(validRequest.getSpecialty());
        copy.setWorkingSchedule(validRequest.getWorkingSchedule());
        return copy;
    }
}