package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

@SpringBootTest
public class AccountTest {

    private Account pacilliansAccount;
    private Account caregiverAccount;

    @BeforeEach
    public void setup() {
        pacilliansAccount = new Account(
                "pacillians@example.com", "password123", "John Doe", "1234567890",
                "123 Street, City", "08123456789", "No allergies", null, null
        );

        caregiverAccount = new Account(
                "caregiver@example.com", "securePassword123", "Dr. Smith", "0987654321",
                "456 Clinic Rd", "08987654321", null, "Cardiology", "Monday 9-5, Tuesday 9-5"
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
        assertEquals(pacilliansAccount.getMedicalHistory(), "No allergies");
        assertNull(pacilliansAccount.getSpeciality());
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
        assertEquals(caregiverAccount.getSpeciality(), "Cardiology");
        assertEquals(caregiverAccount.getWorkingSchedules(), "Monday 9-5, Tuesday 9-5");
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
            new Account("invalid-email", "password123", "Jane Doe", "9876543210",
                    "789 Road, City", "08123456788", null, null, null);
        });
    }

    @Test
    public void testAccountWithMissingPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Account("missingpassword@example.com", "", "Missing Password", "1122334455",
                    "101 Street", "08123456780", null, null, null);
        });
    }
}
