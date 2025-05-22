package id.ac.ui.cs.advprog.b14.pandacare.authentication.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Pacilian pacilian;
    private Caregiver caregiver;

    @BeforeEach
    void setUp() {
        entityManager.clear();
        pacilian = new Pacilian(
                "pacil-123",
                "pacil@example.com",
                "password123",
                "Test Pacilian",
                "1234567890123456",
                "123 Test Street",
                "08123456789",
                Arrays.asList("Allergy")
        );
        caregiver = new Caregiver(
                "care-123",
                "care@example.com",
                "password123",
                "Test Caregiver",
                "6543210987654321",
                "456 Care Street",
                "08987654321",
                "Elderly Care",
                Arrays.asList("Monday 9-5")
        );
    }

    @Test
    void testFindByNonExistentEmail() {
        User found = userRepository.findByEmail("nonexistent@example.com");

        assertNull(found);
    }

    @Test
    void testSaveAndFindByEmail() {
        userRepository.save(caregiver);
        User user = userRepository.findByEmail("care@example.com");

        assertNotNull(user);
        assertEquals(caregiver.getId(), user.getId());
        assertEquals(UserType.CAREGIVER, user.getType());
    }

    @Test
    void testFindAll() {
        entityManager.persist(pacilian);
        entityManager.persist(caregiver);
        entityManager.flush();

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }
}