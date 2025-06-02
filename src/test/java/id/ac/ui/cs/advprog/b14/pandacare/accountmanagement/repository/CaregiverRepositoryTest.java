package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CaregiverRepositoryTest {
    @Autowired
    private CaregiverRepository caregiverRepository;

    private List<Caregiver> caregivers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        List<String> workingSchedules1 = new ArrayList<>();
        workingSchedules1.add("2020-01-01");

        List<String> workingSchedules2 = new ArrayList<>();
        workingSchedules2.add("2020-01-02");

        Caregiver caregiver1 = new Caregiver(
                UUID.randomUUID().toString(),
                "caregiver1@gmail.com",
                "password123!",
                "Caregiver 1",
                "1234567890",
                "Cisauk",
                "1234567890",
                "Specialty 1",
                workingSchedules1
        );
        caregivers.add(caregiver1);

        Caregiver caregiver2 = new Caregiver(
                UUID.randomUUID().toString(),
                "caregiver2@gmail.com",
                "password123!",
                "Caregiver 2",
                "1234567891",
                "Cisauk",
                "1234567890",
                "Specialty 2",
                workingSchedules2
        );
        caregivers.add(caregiver2);
    }

    @Test
    void testSaveCaregiver() {
        Caregiver caregiver = caregivers.getFirst();
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);

        assertNotNull(savedCaregiver);
        assertEquals(caregiver.getId(), savedCaregiver.getId());
        assertEquals(caregiver.getEmail(), savedCaregiver.getEmail());
        assertEquals(caregiver.getAddress(), savedCaregiver.getAddress());
        assertEquals(caregiver.getPhone(), savedCaregiver.getPhone());
        assertEquals(caregiver.getEmail(), savedCaregiver.getEmail());
        assertEquals(caregiver.getName(), savedCaregiver.getName());
    }

    @Test
    void testGetCaregiverById() {
        Caregiver caregiver = caregivers.getFirst();
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        String id = savedCaregiver.getId();

        Optional<Caregiver> foundCaregiver = caregiverRepository.findById(id);
        if (foundCaregiver.isPresent()) {
            assertEquals(caregiver.getId(), foundCaregiver.get().getId());
            assertEquals(caregiver.getEmail(), foundCaregiver.get().getEmail());
            assertEquals(caregiver.getAddress(), foundCaregiver.get().getAddress());
            assertEquals(caregiver.getPhone(), foundCaregiver.get().getPhone());
            assertEquals(caregiver.getName(), foundCaregiver.get().getName());
        }
    }

    @Test
    void testGetAllCaregivers() {
        caregiverRepository.saveAll(caregivers);

        List<Caregiver> allCaregivers = caregiverRepository.findAll();
        assertEquals(caregivers.size(), allCaregivers.size());
    }

    @Test
    void testUpdateCaregiver() {
        Caregiver caregiver1 = caregivers.getFirst();
        Caregiver caregiver2 = caregivers.get(1);
        Caregiver savedCaregiver = caregiverRepository.save(caregiver1);
        String id = savedCaregiver.getId();

        Optional<Caregiver> foundCaregiver = caregiverRepository.findById(id);
        if (foundCaregiver.isPresent()) {
            foundCaregiver.get().setName(caregiver2.getName());
            foundCaregiver.get().setPhone(caregiver2.getPhone());
            foundCaregiver.get().setEmail(caregiver2.getEmail());
            foundCaregiver.get().setAddress(caregiver2.getAddress());
            foundCaregiver.get().setWorkingSchedule(caregiver2.getWorkingSchedule());
            caregiverRepository.save(foundCaregiver.get());

            assertEquals(id, foundCaregiver.get().getId());
            assertEquals(caregiver2.getEmail(), foundCaregiver.get().getEmail());
            assertEquals(caregiver2.getAddress(), foundCaregiver.get().getAddress());
            assertEquals(caregiver2.getPhone(), foundCaregiver.get().getPhone());
            assertEquals(caregiver2.getWorkingSchedule(), foundCaregiver.get().getWorkingSchedule());
        }
    }

    @Test
    void testDeleteCaregiver() {
        Caregiver caregiver = caregivers.getFirst();
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        String id = savedCaregiver.getId();

        caregiverRepository.deleteById(id);

        Optional<Caregiver> foundCaregiver = caregiverRepository.findById(id);
        assertTrue(foundCaregiver.isEmpty());
    }
}
