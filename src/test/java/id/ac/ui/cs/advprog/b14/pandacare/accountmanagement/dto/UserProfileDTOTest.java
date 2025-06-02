package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;

import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.Consultation;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileDTOTest {
    private List<String> medicalHistory = new ArrayList<>();
    private List<WorkingSchedule> schedule = new ArrayList<>();
    private List<Consultation> consultation = new ArrayList<>();

    @Test
    void testDefaultConstructorForPacilian() {
        medicalHistory.addFirst("1");

        UserProfileDTO pacilianDto = new UserProfileDTO(
                UUID.randomUUID().toString(),
                "email@gmail.com",
                "name",
                "1234567890",
                "Cisauk",
                "1234567890",
                UserType.PACILIAN,
                medicalHistory,
                null,
                null,
                null
        );

        assertEquals("email@gmail.com", pacilianDto.getEmail());
        assertEquals("name", pacilianDto.getName());
        assertEquals("1234567890", pacilianDto.getPhone());
        assertEquals("Cisauk", pacilianDto.getAddress());
        assertEquals(UserType.PACILIAN, pacilianDto.getType());
        assertEquals(medicalHistory, pacilianDto.getMedicalHistory());
        assertNull(pacilianDto.getSpecialty());
        assertNull(pacilianDto.getWorkingSchedule());
        assertNull(pacilianDto.getConsultationHistory());
    }

    @Test
    void testDefaultConstructorForCaregiver() {
        WorkingSchedule workingSchedule = new WorkingSchedule(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.MAX,
                "Okay",
                true
        );

        Consultation consultationItem = new Consultation(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.MAX,
                "Okay"
        );

        schedule.addFirst(workingSchedule);
        consultation.addFirst(consultationItem);
        UserProfileDTO caregiverDto = new UserProfileDTO(
                UUID.randomUUID().toString(),
                "email@gmail.com",
                "name",
                "1234567890",
                "Cisauk",
                "1234567890",
                UserType.CAREGIVER,
                null,
                "specialty",
                schedule,
                consultation
        );

        assertEquals("email@gmail.com", caregiverDto.getEmail());
        assertEquals("name", caregiverDto.getName());
        assertEquals("1234567890", caregiverDto.getPhone());
        assertEquals("Cisauk", caregiverDto.getAddress());
        assertEquals(UserType.CAREGIVER, caregiverDto.getType());
        assertEquals("specialty", caregiverDto.getSpecialty());
        assertTrue(medicalHistory.isEmpty());
        assertNotNull(caregiverDto.getWorkingSchedule());
        assertNotNull(caregiverDto.getConsultationHistory());
    }

}
