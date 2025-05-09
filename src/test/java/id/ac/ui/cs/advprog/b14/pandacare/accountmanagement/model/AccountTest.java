package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private UUID id;
    private String email;
    private String password;
    private String name;
    private String nik;
    private String address;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        email = "pacilian@example.com";
        password = "securePass123";
        name = "Jane Doe";
        nik = "1234567890123456";
        address = "Jl. Pandacare No.1";
        phoneNumber = "08123456789";
    }

    @Test
    void testBuildPacilianAccount() {
        List<String> medicalHistory = Arrays.asList("Diabetes", "Hypertension");

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nik(nik)
                .address(address)
                .phoneNumber(phoneNumber)
                .userType(UserType.PACILIAN)
                .medicalHistory(medicalHistory)
                .build();

        assertEquals(UserType.PACILIAN, account.getUserType());
        assertEquals(medicalHistory, account.getMedicalHistory());
        assertNull(account.getSpecialty());
        assertNull(account.getWorkingSchedules());
    }

    @Test
    void testBuildCaregiverAccount() {
        List<String> schedules = Arrays.asList("Monday 9-12", "Tuesday 13-17");

        Account account = Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nik(nik)
                .address(address)
                .phoneNumber(phoneNumber)
                .userType(UserType.CAREGIVER)
                .specialty("Neurology")
                .workingSchedules(schedules)
                .build();

        assertEquals(UserType.CAREGIVER, account.getUserType());
        assertEquals("Neurology", account.getSpecialty());
        assertEquals(schedules, account.getWorkingSchedules());
        assertNull(account.getMedicalHistory());
    }

    @Test
    void testEqualsAndHashCode() {
        Account acc1 = Account.builder().id(id).email(email).build();
        Account acc2 = Account.builder().id(id).email(email).build();

        assertEquals(acc1, acc2);
        assertEquals(acc1.hashCode(), acc2.hashCode());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Account account = new Account();
        account.setId(id);
        account.setEmail(email);
        account.setPassword(password);
        account.setName(name);
        account.setNik(nik);
        account.setAddress(address);
        account.setPhoneNumber(phoneNumber);
        account.setUserType(UserType.PACILIAN);
        account.setMedicalHistory(Collections.singletonList("Asthma"));
        account.setSpecialty(null);
        account.setWorkingSchedules(null);

        assertNotNull(account.getId());
        assertEquals(UserType.PACILIAN, account.getUserType());
        assertEquals("Asthma", account.getMedicalHistory().get(0));
    }

    @Test
    void testNullFieldsAllowedForOptionalData() {
        Account account = Account.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nik(nik)
                .address(address)
                .phoneNumber(phoneNumber)
                .userType(UserType.PACILIAN)
                .medicalHistory(null)
                .specialty(null)
                .workingSchedules(null)
                .build();

        assertNull(account.getMedicalHistory());
        assertNull(account.getSpecialty());
        assertNull(account.getWorkingSchedules());
    }
}
