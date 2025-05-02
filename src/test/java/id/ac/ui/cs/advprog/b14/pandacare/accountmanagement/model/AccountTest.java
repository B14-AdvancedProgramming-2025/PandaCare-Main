package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.UUID;

public class AccountTest {

    private Account pacilliansAccount;
    private Account caregiverAccount;

    @BeforeEach
    public void setup() {
        pacilliansAccount = new Account(
                UUID.randomUUID(), "pacillians@example.com", "password123", "John Doe", "1234567890",
                "123 Street, City", "08123456789", "PACILIAN",
                Arrays.asList("No allergies"), null, null
        );

        caregiverAccount = new Account(
                UUID.randomUUID(), "caregiver@example.com", "securePassword123", "Dr. Smith", "0987654321",
                "456 Clinic Rd", "08987654321", "CAREGIVER",
                null, "Cardiology", Arrays.asList("Monday 9-5", "Tuesday 9-5")
        );
    }

    @Test
    public void testCreatePacilliansAccount() {
        assertNotNull(pacilliansAccount);
        assertNotNull(pacilliansAccount.getId());
        assertEquals(pacilliansAccount.getEmail(), "pacillians@example.com");
        assertEquals(pacilliansAccount.getName(), "John Doe");
        assertEquals(pacilliansAccount.getNik(), "1234567890");
        assertEquals(pacilliansAccount.getAddress(), "123 Street, City");
        assertEquals(pacilliansAccount.getPhoneNumber(), "08123456789");
        assertEquals(pacilliansAccount.getUserType(), "PACILIAN");
        assertEquals(pacilliansAccount.getMedicalHistory(), Arrays.asList("No allergies"));
        assertNull(pacilliansAccount.getSpecialty());
        assertNull(pacilliansAccount.getWorkingSchedules());
    }

    @Test
    public void testCreateCaregiverAccount() {
        assertNotNull(caregiverAccount);
        assertNotNull(caregiverAccount.getId());
        assertEquals(caregiverAccount.getEmail(), "caregiver@example.com");
        assertEquals(caregiverAccount.getName(), "Dr. Smith");
        assertEquals(caregiverAccount.getNik(), "0987654321");
        assertEquals(caregiverAccount.getAddress(), "456 Clinic Rd");
        assertEquals(caregiverAccount.getPhoneNumber(), "08987654321");
        assertEquals(caregiverAccount.getUserType(), "CAREGIVER");
        assertEquals(caregiverAccount.getSpecialty(), "Cardiology");
        assertEquals(caregiverAccount.getWorkingSchedules(), Arrays.asList("Monday 9-5", "Tuesday 9-5"));
        assertNull(caregiverAccount.getMedicalHistory());
    }

    @Test
    public void testUUIDGeneration() {
        assertNotNull(pacilliansAccount.getId());
        assertNotNull(caregiverAccount.getId());
        assertNotEquals(pacilliansAccount.getId(), caregiverAccount.getId());
    }

    @Test
    public void testInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Account(UUID.randomUUID(), "invalid-email", "password123", "Jane Doe", "9876543210",
                    "789 Road, City", "08123456788", "PACILIAN",
                    null, null, null);
        });
    }

    @Test
    public void testAccountWithMissingPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Account(UUID.randomUUID(), "missingpassword@example.com", "", "Missing Password", "1122334455",
                    "101 Street", "08123456780", "PACILIAN",
                    null, null, null);
        });
    }
}
