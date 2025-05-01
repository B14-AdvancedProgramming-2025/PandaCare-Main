package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateUser() {
        List<String> medicalHistory = List.of("Heart Attack", "Diabetes");
        Pacilian user1 = new Pacilian("pacilian@gmail.com", "password123", "John Doe", "1234567890123456", "123 Main St", "08123456789", medicalHistory);
        assertEquals("pacilian@gmail.com", user1.getEmail());
        assertEquals("password123", user1.getPassword());
        assertEquals("John Doe", user1.getName());
        assertEquals("1234567890123456", user1.getNik());
        assertEquals("123 Main St", user1.getAddress());
        assertEquals("08123456789", user1.getPhone());
        assertEquals(medicalHistory, user1.getMedicalHistory());
    }
}