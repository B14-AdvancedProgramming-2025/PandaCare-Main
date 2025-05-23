package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverTest {

    @Test
    void testCaregiverConstructor() {
        String id = "care-123";
        String email = "caregiver@example.com";
        String password = "password123";
        String name = "Care Giver";
        String nik = "6543210987654321";
        String address = "456 Care Street";
        String phone = "08987654321";
        String specialty = "Elderly Care";
        List<String> workingSchedule = Arrays.asList("Monday 9-5", "Wednesday 9-5", "Friday 9-5");

        Caregiver caregiver = new Caregiver(id, email, password, name, nik, address, phone, specialty, workingSchedule);

        assertEquals(id, caregiver.getId());
        assertEquals(email, caregiver.getEmail());
        assertEquals(password, caregiver.getPassword());
        assertEquals(name, caregiver.getName());
        assertEquals(nik, caregiver.getNik());
        assertEquals(address, caregiver.getAddress());
        assertEquals(phone, caregiver.getPhone());
        assertEquals(UserType.CAREGIVER, caregiver.getType());
        assertEquals(specialty, caregiver.getSpecialty());
        assertEquals(workingSchedule, caregiver.getWorkingSchedule());
    }

    @Test
    void testSpecialtyAndWorkingScheduleSetters() {
        Caregiver caregiver = new Caregiver();
        String specialty = "Physical Therapy";
        List<String> workingSchedule = Arrays.asList("Tuesday 10-6", "Thursday 10-6");

        caregiver.setSpecialty(specialty);
        caregiver.setWorkingSchedule(workingSchedule);

        assertEquals(specialty, caregiver.getSpecialty());
        assertEquals(workingSchedule, caregiver.getWorkingSchedule());
    }

    @Test
    void testCaregiverInheritanceFromUser() {
        Caregiver caregiver = new Caregiver();
        assertInstanceOf(User.class, caregiver);
    }

    @Test
    void testCaregiverRoleIsCorrect() {
        Caregiver caregiver = new Caregiver();
        caregiver.setType(UserType.CAREGIVER);

        assertEquals("CAREGIVER", caregiver.getRole());
    }
}