package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PacilianTest {

    @Test
    void testPacilianConstructor() {
        String id = "pacil-123";
        String email = "pacil@example.com";
        String password = "password123";
        String name = "Pacil User";
        String nik = "1234567890123456";
        String address = "123 Pacil Street";
        String phone = "08123456789";
        List<String> medicalHistory = Arrays.asList("Allergy", "Asthma");

        Pacilian pacilian = new Pacilian(id, email, password, name, nik, address, phone, medicalHistory);

        assertEquals(id, pacilian.getId());
        assertEquals(email, pacilian.getEmail());
        assertEquals(password, pacilian.getPassword());
        assertEquals(name, pacilian.getName());
        assertEquals(nik, pacilian.getNik());
        assertEquals(address, pacilian.getAddress());
        assertEquals(phone, pacilian.getPhone());
        assertEquals(UserType.PACILIAN, pacilian.getType());
        assertEquals(medicalHistory, pacilian.getMedicalHistory());
    }

    @Test
    void testMedicalHistorySetter() {
        Pacilian pacilian = new Pacilian();
        List<String> medicalHistory = Arrays.asList("Diabetes", "Hypertension");

        pacilian.setMedicalHistory(medicalHistory);

        assertEquals(medicalHistory, pacilian.getMedicalHistory());
    }

    @Test
    void testPacilianInheritanceFromUser() {
        Pacilian pacilian = new Pacilian();
        assertInstanceOf(User.class, pacilian);
    }

    @Test
    void testPacilianRoleIsCorrect() {
        Pacilian pacilian = new Pacilian();
        pacilian.setType(UserType.PACILIAN);

        assertEquals("PACILIAN", pacilian.getRole());
    }
}