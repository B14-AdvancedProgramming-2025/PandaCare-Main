package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PacilianRepositoryTest {
    @Autowired
    private PacilianRepository pacilianRepository;
    private List<Pacilian> pacilianList;

    @BeforeEach
    void setUp() {
        pacilianList = new ArrayList<>();

        List<String> medicalHistory = new ArrayList<>();
        medicalHistory.add(UUID.randomUUID().toString());

        Pacilian pacilian1 = new Pacilian(
                UUID.randomUUID().toString(),
                "pacilian1@gmail.com",
                "password123!",
                "Pacilian 1",
                "1234567890",
                "Cisauk",
                "1234567890",
                medicalHistory
        );

        pacilianList.add(pacilian1);

        Pacilian pacilian2 = new Pacilian(
                UUID.randomUUID().toString(),
                "pacilian2@gmail.com",
                "password123!",
                "Pacilian 2",
                "1234567891",
                "Cisauk",
                "1234567890",
                medicalHistory
        );

        pacilianList.add(pacilian2);
    }

    @Test
    void testAddPacilian() {
        Pacilian pacilian = pacilianList.getFirst();
        Pacilian savedPacilian = pacilianRepository.save(pacilian);

        assertNotNull(pacilian);
        assertEquals(pacilian.getId(), savedPacilian.getId());
        assertEquals(pacilian.getPassword(), savedPacilian.getPassword());
        assertEquals(pacilian.getEmail(), savedPacilian.getEmail());
        assertEquals(pacilian.getName(), savedPacilian.getName());
        assertEquals(pacilian.getPhone(), savedPacilian.getPhone());
        assertEquals(pacilian.getAddress(), savedPacilian.getAddress());
    }

    @Test
    void testGetPacilianById() {
        Pacilian pacilian = pacilianList.getFirst();
        Pacilian savedPacilian = pacilianRepository.save(pacilian);
        String id = pacilian.getId();

        Optional<Pacilian> foundPacilian = pacilianRepository.findById(id);
        if (foundPacilian.isPresent()) {
            assertEquals(pacilian.getId(), foundPacilian.get().getId());
            assertEquals(pacilian.getEmail(), foundPacilian.get().getEmail());
            assertEquals(pacilian.getName(), foundPacilian.get().getName());
            assertEquals(pacilian.getPhone(), foundPacilian.get().getPhone());
            assertEquals(pacilian.getAddress(), foundPacilian.get().getAddress());
        }
    }

    @Test
    void testGetAllPacilians() {
        pacilianRepository.saveAll(pacilianList);

        List<Pacilian> allPacilians = pacilianRepository.findAll();
        assertEquals(pacilianList.size(), allPacilians.size());
    }

    @Test
    void testUpdatePacilian() {
        Pacilian pacilian = pacilianList.getFirst();
        Pacilian pacilian2 = pacilianList.get(1);
        pacilianRepository.save(pacilian);
        String id = pacilian.getId();

        Optional<Pacilian> foundPacilian = pacilianRepository.findById(id);
        if (foundPacilian.isPresent()) {
            foundPacilian.get().setName(pacilian2.getName());
            foundPacilian.get().setPhone(pacilian2.getPhone());
            foundPacilian.get().setAddress(pacilian2.getAddress());
            foundPacilian.get().setEmail(pacilian2.getEmail());
            pacilianRepository.save(foundPacilian.get());

            assertEquals(id, foundPacilian.get().getId());
            assertEquals(pacilian2.getName(), foundPacilian.get().getName());
            assertEquals(pacilian2.getPhone(), foundPacilian.get().getPhone());
            assertEquals(pacilian2.getAddress(), foundPacilian.get().getAddress());
            assertEquals(pacilian2.getEmail(), foundPacilian.get().getEmail());
        }
    }

    @Test
    void testDeletePacilian() {
        Pacilian pacilian = pacilianList.getFirst();
        Pacilian savedPacilian = pacilianRepository.save(pacilian);
        String id = pacilian.getId();

        pacilianRepository.deleteById(id);

        Optional<Pacilian> foundPacilian = pacilianRepository.findById(id);
        assertTrue(foundPacilian.isEmpty());
    }
}
