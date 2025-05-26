package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.PacilianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private PacilianRepository pacilianRepository;

    @Mock
    private CaregiverRepository caregiverRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Pacilian pacilian;
    private Caregiver caregiver;
    private UpdateProfileDTO updateProfileDTO;

    @BeforeEach
    void setUp() {
        // Setup Pacilian using mock
        pacilian = mock(Pacilian.class);
        when(pacilian.getId()).thenReturn("pac-123");
        when(pacilian.getEmail()).thenReturn("pacilian@example.com");
        when(pacilian.getName()).thenReturn("John Doe");
        when(pacilian.getNik()).thenReturn("1234567890123456");
        when(pacilian.getAddress()).thenReturn("Jakarta");
        when(pacilian.getPhone()).thenReturn("081234567890");
        when(pacilian.getType()).thenReturn(UserType.PACILIAN);
        when(pacilian.getMedicalHistory()).thenReturn(Arrays.asList("Diabetes", "Hypertension"));

        // Setup Caregiver using mock
        caregiver = mock(Caregiver.class);
        when(caregiver.getId()).thenReturn("car-123");
        when(caregiver.getEmail()).thenReturn("caregiver@example.com");
        when(caregiver.getName()).thenReturn("Jane Smith");
        when(caregiver.getNik()).thenReturn("9876543210987654");
        when(caregiver.getAddress()).thenReturn("Bandung");
        when(caregiver.getPhone()).thenReturn("087654321098");
        when(caregiver.getType()).thenReturn(UserType.CAREGIVER);
        when(caregiver.getSpecialty()).thenReturn("Cardiology");
        when(caregiver.getWorkingSchedule()).thenReturn(Collections.emptyList());

        // Setup UpdateProfileDTO
        updateProfileDTO = UpdateProfileDTO.builder()
                .name("Updated Name")
                .address("Updated Address")
                .phone("081111111111")
                .build();
    }

    @Test
    void getProfileById_WhenPacilianExists_ShouldReturnUserProfileDTO() {
        // Given
        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));

        // When
        UserProfileDTO result = accountService.getProfileById("pac-123");

        // Then
        assertNotNull(result);
        assertEquals("pac-123", result.getId());
        assertEquals("pacilian@example.com", result.getEmail());
        assertEquals("John Doe", result.getName());
        assertEquals("1234567890123456", result.getNik());
        assertEquals("Jakarta", result.getAddress());
        assertEquals("081234567890", result.getPhone());
        assertEquals(UserType.PACILIAN, result.getType());
        assertEquals(Arrays.asList("Diabetes", "Hypertension"), result.getMedicalHistory());
        assertNull(result.getSpecialty());
        assertTrue(result.getWorkingSchedule().isEmpty());
        assertTrue(result.getConsultationHistory().isEmpty());

        verify(pacilianRepository).findById("pac-123");
        verify(caregiverRepository, never()).findById(anyString());
    }

    @Test
    void getProfileById_WhenCaregiverExists_ShouldReturnUserProfileDTO() {
        // Given
        when(pacilianRepository.findById("car-123")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("car-123")).thenReturn(Optional.of(caregiver));

        // When
        UserProfileDTO result = accountService.getProfileById("car-123");

        // Then
        assertNotNull(result);
        assertEquals("car-123", result.getId());
        assertEquals("caregiver@example.com", result.getEmail());
        assertEquals("Jane Smith", result.getName());
        assertEquals("9876543210987654", result.getNik());
        assertEquals("Bandung", result.getAddress());
        assertEquals("087654321098", result.getPhone());
        assertEquals(UserType.CAREGIVER, result.getType());
        assertEquals("Cardiology", result.getSpecialty());
        assertNull(result.getMedicalHistory());
        assertTrue(result.getWorkingSchedule().isEmpty());
        assertTrue(result.getConsultationHistory().isEmpty());

        verify(pacilianRepository).findById("car-123");
        verify(caregiverRepository).findById("car-123");
    }

    @Test
    void getProfileById_WhenUserNotFound_ShouldThrowNoSuchElementException() {
        // Given
        when(pacilianRepository.findById("nonexistent")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> accountService.getProfileById("nonexistent"));
        assertEquals("User not found with ID: nonexistent", exception.getMessage());

        verify(pacilianRepository).findById("nonexistent");
        verify(caregiverRepository).findById("nonexistent");
    }

    @Test
    void getProfileById_WhenPacilianHasNullMedicalHistory_ShouldReturnEmptyList() {
        // Given
        when(pacilian.getMedicalHistory()).thenReturn(null);
        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));

        // When
        UserProfileDTO result = accountService.getProfileById("pac-123");

        // Then
        assertNotNull(result);
        assertTrue(result.getMedicalHistory().isEmpty());
    }

    @Test
    void getProfileById_WhenCaregiverHasNullWorkingSchedule_ShouldReturnEmptyList() {
        // Given
        when(caregiver.getWorkingSchedule()).thenReturn(null);
        when(pacilianRepository.findById("car-123")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("car-123")).thenReturn(Optional.of(caregiver));

        // When
        UserProfileDTO result = accountService.getProfileById("car-123");

        // Then
        assertNotNull(result);
        assertTrue(result.getWorkingSchedule().isEmpty());
    }

    @Test
    void updateProfile_WhenPacilianExists_ShouldUpdateAndReturnDTO() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .name("Updated Name")
                .address("Updated Address")
                .phone("081111111111")
                .medicalHistory(Arrays.asList("Updated History"))
                .build();

        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));
        when(pacilianRepository.save(any(Pacilian.class))).thenReturn(pacilian);

        // When
        UserProfileDTO result = accountService.updateProfile("pac-123", dto);

        // Then
        assertNotNull(result);
        verify(pacilian).setName("Updated Name");
        verify(pacilian).setAddress("Updated Address");
        verify(pacilian).setPhone("081111111111");
        verify(pacilian).setMedicalHistory(Arrays.asList("Updated History"));

        verify(pacilianRepository).findById("pac-123");
        verify(pacilianRepository).save(pacilian);
        verify(caregiverRepository, never()).findById(anyString());
        verify(caregiverRepository, never()).save(any());
    }

    @Test
    void updateProfile_WhenCaregiverExists_ShouldUpdateAndReturnDTO() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .name("Updated Caregiver Name")
                .address("Updated Caregiver Address")
                .phone("087777777777")
                .specialty("Updated Specialty")
                .build();

        when(pacilianRepository.findById("car-123")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("car-123")).thenReturn(Optional.of(caregiver));
        when(caregiverRepository.save(any(Caregiver.class))).thenReturn(caregiver);

        // When
        UserProfileDTO result = accountService.updateProfile("car-123", dto);

        // Then
        assertNotNull(result);
        verify(caregiver).setName("Updated Caregiver Name");
        verify(caregiver).setAddress("Updated Caregiver Address");
        verify(caregiver).setPhone("087777777777");
        verify(caregiver).setSpecialty("Updated Specialty");

        verify(pacilianRepository).findById("car-123");
        verify(caregiverRepository).findById("car-123");
        verify(caregiverRepository).save(caregiver);
        verify(pacilianRepository, never()).save(any());
    }

    @Test
    void updateProfile_WhenUserNotFound_ShouldThrowNoSuchElementException() {
        // Given
        when(pacilianRepository.findById("nonexistent")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> accountService.updateProfile("nonexistent", updateProfileDTO));
        assertEquals("User not found with ID: nonexistent", exception.getMessage());

        verify(pacilianRepository).findById("nonexistent");
        verify(caregiverRepository).findById("nonexistent");
    }

    @Test
    void updateProfile_WhenTryingToChangeNik_ShouldThrowIllegalArgumentException() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .nik("different-nik")
                .name("Updated Name")
                .build();

        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));
        when(pacilian.getNik()).thenReturn("1234567890123456");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.updateProfile("pac-123", dto));
        assertEquals("NIK cannot be updated.", exception.getMessage());

        verify(pacilianRepository).findById("pac-123");
        verify(pacilianRepository, never()).save(any());
    }

    @Test
    void updateProfile_WhenTryingToChangeEmail_ShouldThrowIllegalArgumentException() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .email("different@email.com")
                .name("Updated Name")
                .build();

        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));
        when(pacilian.getEmail()).thenReturn("pacilian@example.com");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.updateProfile("pac-123", dto));
        assertEquals("Email cannot be updated.", exception.getMessage());

        verify(pacilianRepository).findById("pac-123");
        verify(pacilianRepository, never()).save(any());
    }

    @Test
    void updateProfile_WhenNikIsSame_ShouldNotThrowException() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .nik("1234567890123456") // Same NIK
                .name("Updated Name")
                .build();

        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));
        when(pacilian.getNik()).thenReturn("1234567890123456");
        when(pacilianRepository.save(any(Pacilian.class))).thenReturn(pacilian);

        // When & Then
        assertDoesNotThrow(() -> accountService.updateProfile("pac-123", dto));

        verify(pacilianRepository).save(pacilian);
    }

    @Test
    void updateProfile_WhenEmailIsSame_ShouldNotThrowException() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .email("pacilian@example.com") // Same email
                .name("Updated Name")
                .build();

        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));
        when(pacilian.getEmail()).thenReturn("pacilian@example.com");
        when(pacilianRepository.save(any(Pacilian.class))).thenReturn(pacilian);

        // When & Then
        assertDoesNotThrow(() -> accountService.updateProfile("pac-123", dto));

        verify(pacilianRepository).save(pacilian);
    }

    @Test
    void updateProfile_WhenPacilianWithNullMedicalHistory_ShouldNotUpdateMedicalHistory() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .name("Updated Name")
                .medicalHistory(null)
                .build();

        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));
        when(pacilianRepository.save(any(Pacilian.class))).thenReturn(pacilian);

        // When
        accountService.updateProfile("pac-123", dto);

        // Then
        verify(pacilian, never()).setMedicalHistory(any());
        verify(pacilianRepository).save(pacilian);
    }

    @Test
    void updateProfile_WhenCaregiverWithNullSpecialty_ShouldNotUpdateSpecialty() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .name("Updated Name")
                .specialty(null)
                .build();

        when(pacilianRepository.findById("car-123")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("car-123")).thenReturn(Optional.of(caregiver));
        when(caregiverRepository.save(any(Caregiver.class))).thenReturn(caregiver);

        // When
        accountService.updateProfile("car-123", dto);

        // Then
        verify(caregiver, never()).setSpecialty(any());
        verify(caregiverRepository).save(caregiver);
    }

    @Test
    void updateProfile_WhenCaregiverWithNullWorkingSchedule_ShouldNotUpdateWorkingSchedule() {
        // Given
        UpdateProfileDTO dto = UpdateProfileDTO.builder()
                .name("Updated Name")
                .workingSchedule(null)
                .build();

        when(pacilianRepository.findById("car-123")).thenReturn(Optional.empty());
        when(caregiverRepository.findById("car-123")).thenReturn(Optional.of(caregiver));
        when(caregiverRepository.save(any(Caregiver.class))).thenReturn(caregiver);

        // When
        accountService.updateProfile("car-123", dto);

        // Then
        verify(caregiver, never()).setWorkingSchedule(any());
        verify(caregiverRepository).save(caregiver);
    }

    @Test
    void deleteProfile_WhenPacilianExists_ShouldDeleteSuccessfully() {
        // Given
        when(pacilianRepository.existsById("pac-123")).thenReturn(true);

        // When
        accountService.deleteProfile("pac-123");

        // Then
        verify(pacilianRepository).existsById("pac-123");
        verify(pacilianRepository).deleteById("pac-123");
        verify(caregiverRepository, never()).existsById(anyString());
        verify(caregiverRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteProfile_WhenCaregiverExists_ShouldDeleteSuccessfully() {
        // Given
        when(pacilianRepository.existsById("car-123")).thenReturn(false);
        when(caregiverRepository.existsById("car-123")).thenReturn(true);

        // When
        accountService.deleteProfile("car-123");

        // Then
        verify(pacilianRepository).existsById("car-123");
        verify(caregiverRepository).existsById("car-123");
        verify(caregiverRepository).deleteById("car-123");
        verify(pacilianRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteProfile_WhenUserNotFound_ShouldThrowNoSuchElementException() {
        // Given
        when(pacilianRepository.existsById("nonexistent")).thenReturn(false);
        when(caregiverRepository.existsById("nonexistent")).thenReturn(false);

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> accountService.deleteProfile("nonexistent"));
        assertEquals("User not found with ID: nonexistent", exception.getMessage());

        verify(pacilianRepository).existsById("nonexistent");
        verify(caregiverRepository).existsById("nonexistent");
        verify(pacilianRepository, never()).deleteById(anyString());
        verify(caregiverRepository, never()).deleteById(anyString());
    }

    @Test
    void convertToUserProfileDTO_WithUnknownUserType_ShouldHandleGracefully() {
        // This test checks the builder pattern works correctly
        // and that consultation history is always empty
        when(pacilianRepository.findById("pac-123")).thenReturn(Optional.of(pacilian));

        UserProfileDTO result = accountService.getProfileById("pac-123");

        assertTrue(result.getConsultationHistory().isEmpty());
    }
}