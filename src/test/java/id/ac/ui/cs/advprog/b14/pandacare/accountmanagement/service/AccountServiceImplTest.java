package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.PacilianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    private PacilianRepository pacilianRepository;
    private CaregiverRepository caregiverRepository;
    private AccountServiceImpl accountService;

    private Pacilian testPacilian;
    private Caregiver testCaregiver;

    @BeforeEach
    void setUp() {
        pacilianRepository = mock(PacilianRepository.class);
        caregiverRepository = mock(CaregiverRepository.class);
        accountService = new AccountServiceImpl(pacilianRepository, caregiverRepository);

        testPacilian = Pacilian.builder()
                .id("pacilian-1")
                .email("pacilian@example.com")
                .password("secure")
                .name("Pacilian A")
                .nik("1234567890")
                .address("Jalan Sehat")
                .phone("081234567890")
                .medicalHistory(List.of("Diabetes"))
                .build();

        testCaregiver = Caregiver.builder()
                .id("caregiver-1")
                .email("caregiver@example.com")
                .password("secure")
                .name("Caregiver B")
                .nik("0987654321")
                .address("Jalan Rawat")
                .phone("089876543210")
                .specialty("Orthopedic")
                .workingSchedule(List.of("Monday", "Wednesday"))
                .build();
    }

    @Test
    void testGetProfileByIdPacilianFound() {
        when(pacilianRepository.findById("pacilian-1")).thenReturn(Optional.of(testPacilian));
        User result = accountService.getProfileById("pacilian-1");

        assertNotNull(result);
        assertEquals(testPacilian.getEmail(), result.getEmail());
    }

    @Test
    void testGetProfileByIdCaregiverFound() {
        when(pacilianRepository.findById("caregiver-1")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("caregiver-1")).thenReturn(Optional.of(testCaregiver));
        User result = accountService.getProfileById("caregiver-1");

        assertNotNull(result);
        assertEquals(testCaregiver.getEmail(), result.getEmail());
    }

    @Test
    void testGetProfileByIdNotFound() {
        when(pacilianRepository.findById("unknown")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> accountService.getProfileById("unknown"));
    }

    @Test
    void testUpdateProfilePacilianSuccess() {
        Pacilian updatedPacilian = Pacilian.builder()
                .id("pacilian-1")
                .email("new@example.com")
                .password("newpass")
                .name("Updated Name")
                .nik("1234567890") // same NIK
                .address("Updated Street")
                .phone("089999999999")
                .medicalHistory(List.of("Hypertension"))
                .build();

        when(pacilianRepository.findById("pacilian-1")).thenReturn(Optional.of(testPacilian));
        when(pacilianRepository.save(any(Pacilian.class))).thenReturn(updatedPacilian);

        User result = accountService.updateProfile("pacilian-1", updatedPacilian);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        verify(pacilianRepository, times(1)).save(any(Pacilian.class));
    }

    @Test
    void testUpdateProfilePacilianNIKChanged() {
        Pacilian updatedPacilian = Pacilian.builder()
                .id("pacilian-1")
                .email("new@example.com")
                .nik("9999999999") // different NIK
                .build();

        when(pacilianRepository.findById("pacilian-1")).thenReturn(Optional.of(testPacilian));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.updateProfile("pacilian-1", updatedPacilian));
    }

    @Test
    void testUpdateProfileCaregiverSuccess() {
        Caregiver updatedCaregiver = Caregiver.builder()
                .id("caregiver-1")
                .email("caregiver.updated@example.com")
                .password("updatedpass")
                .name("Updated Caregiver")
                .nik("0987654321") // same NIK
                .address("New Address")
                .phone("087777777777")
                .specialty("Neurology")
                .workingSchedule(List.of("Tuesday"))
                .build();

        when(caregiverRepository.findById("caregiver-1")).thenReturn(Optional.of(testCaregiver));
        when(caregiverRepository.save(any(Caregiver.class))).thenReturn(updatedCaregiver);

        User result = accountService.updateProfile("caregiver-1", updatedCaregiver);

        assertNotNull(result);
        assertEquals("Neurology", ((Caregiver) result).getSpecialty());
    }

    @Test
    void testDeleteProfilePacilian() {
        when(pacilianRepository.existsById("pacilian-1")).thenReturn(true);
        doNothing().when(pacilianRepository).deleteById("pacilian-1");

        assertDoesNotThrow(() -> accountService.deleteProfile("pacilian-1"));
        verify(pacilianRepository).deleteById("pacilian-1");
    }

    @Test
    void testDeleteProfileCaregiver() {
        when(pacilianRepository.existsById("caregiver-1")).thenReturn(false);
        when(caregiverRepository.existsById("caregiver-1")).thenReturn(true);
        doNothing().when(caregiverRepository).deleteById("caregiver-1");

        assertDoesNotThrow(() -> accountService.deleteProfile("caregiver-1"));
        verify(caregiverRepository).deleteById("caregiver-1");
    }

    @Test
    void testDeleteProfileNotFound() {
        when(pacilianRepository.existsById("unknown")).thenReturn(false);
        when(caregiverRepository.existsById("unknown")).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> accountService.deleteProfile("unknown"));
    }
}
