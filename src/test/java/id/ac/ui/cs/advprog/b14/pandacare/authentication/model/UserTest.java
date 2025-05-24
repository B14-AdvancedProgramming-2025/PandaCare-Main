package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        String id = "user-123";
        String email = "test@example.com";
        String password = "password123";
        String name = "Test User";
        String nik = "1234567890123456";
        String address = "123 Test Street";
        String phone = "08123456789";
        List<String> medicalHistory = Arrays.asList("Allergy", "Asthma");

        Pacilian user = new Pacilian(id, email, password, name, nik, address, phone, medicalHistory);

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(nik, user.getNik());
        assertEquals(address, user.getAddress());
        assertEquals(phone, user.getPhone());
        assertEquals(UserType.PACILIAN, user.getType());
    }

    @Test
    void testUserSetters() {
        Pacilian user = new Pacilian();
        String id = "user-123";
        String email = "test@example.com";
        String password = "password123";
        String name = "Test User";
        String nik = "1234567890123456";
        String address = "123 Test Street";
        String phone = "08123456789";

        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setNik(nik);
        user.setAddress(address);
        user.setPhone(phone);
        user.setType(UserType.PACILIAN);

        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(nik, user.getNik());
        assertEquals(address, user.getAddress());
        assertEquals(phone, user.getPhone());
        assertEquals(UserType.PACILIAN, user.getType());
    }

    @Test
    void testGetRole() {
        Pacilian pacilian = new Pacilian();
        pacilian.setType(UserType.PACILIAN);

        Caregiver caregiver = new Caregiver();
        caregiver.setType(UserType.CAREGIVER);

        assertEquals("PACILIAN", pacilian.getRole());
        assertEquals("CAREGIVER", caregiver.getRole());
    }
}