package id.ac.ui.cs.advprog.b14.pandacare.authentication.repository;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
}