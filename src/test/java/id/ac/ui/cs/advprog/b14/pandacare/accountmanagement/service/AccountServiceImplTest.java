package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository.PacilianRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private CaregiverRepository caregiverRepository;

    @Mock
    private PacilianRepository pacilianRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Caregiver caregiver1;
    private Caregiver caregiver2;

    @BeforeEach
    void setUp() {
        List<String> workingSchedule = new ArrayList<>();
        workingSchedule.add(UUID.randomUUID().toString());
        workingSchedule.add(UUID.randomUUID().toString());

        caregiver1 = new Caregiver(
                UUID.randomUUID().toString(),
                "caregiver1@gmail.com",
                "password123!",
                "Caregiver 1",
                "1234567890",
                "Cisauk",
                "1234567890",
                "Specialty 1",
                workingSchedule
        );

        caregiver2 = new Caregiver(
                caregiver1.getId(),
                "caregiver2@gmail.com",
                "password123!",
                "Caregiver 2",
                "1234567890",
                "Cisauk",
                "1234567890",
                "Specialty 2",
                workingSchedule
        );


    }

    @Test
    public void testGetProfileById_Found() {
        Caregiver caregiver = caregiver1;
        doReturn(Optional.of(caregiver)).when(caregiverRepository).findById(caregiver.getId());

        UserProfileDTO retrievedCaregiver = accountService.getProfileById(caregiver.getId());

        assertNotNull(retrievedCaregiver);
        assertEquals(caregiver.getId(), retrievedCaregiver.getId());
        verify(caregiverRepository, times(1)).findById(caregiver.getId());
    }

    @Test
    public void testGetProfileById_NotFound() {
        assertThrows(NoSuchElementException.class, () -> accountService.getProfileById(UUID.randomUUID().toString()));
    }

    @Test
    public void testFindUserByEmail_Found() {
        Caregiver caregiver = caregiver1;
        doReturn(Optional.of(caregiver1)).when(caregiverRepository).findByEmail(caregiver.getEmail());

        User retrievedCaregiver = accountService.findUserByEmail(caregiver.getEmail());

        assertNotNull(retrievedCaregiver);
        assertEquals(caregiver.getId(), retrievedCaregiver.getId());
        verify(caregiverRepository, times(1)).findByEmail(caregiver.getEmail());
    }

    @Test
    public void testFindUserByEmail_NotFound() {
        doReturn(Optional.empty()).when(caregiverRepository).findByEmail("randomemail@gmail.com");

        assertNull(accountService.findUserByEmail("randomemail@gmail.com"));
    }

    @Test
    public void testUpdateProfile_Found() {
        Caregiver caregiver = caregiver1;
        Caregiver updatedCaregiver = caregiver2;

        when(caregiverRepository.findById(caregiver.getId())).thenReturn(Optional.of(caregiver));

        UpdateProfileDTO updatedProfileDTO = UpdateProfileDTO.builder().build();
        updatedProfileDTO.setEmail(updatedCaregiver.getEmail());
        updatedProfileDTO.setNik(updatedCaregiver.getNik());
        updatedProfileDTO.setAddress(updatedCaregiver.getAddress());
        updatedProfileDTO.setEmail(updatedCaregiver.getEmail());
        updatedProfileDTO.setSpecialty(updatedCaregiver.getSpecialty());
        updatedProfileDTO.setWorkingSchedule(updatedCaregiver.getWorkingSchedule());
        updatedProfileDTO.setType(updatedCaregiver.getType());
        updatedProfileDTO.setPhone(updatedCaregiver.getPhone());

        UserProfileDTO result = accountService.updateProfile(caregiver.getId(), updatedProfileDTO);

        assertNotNull(result);
        assertEquals(caregiver.getId(), result.getId());
        verify(caregiverRepository, times(1)).findById(caregiver.getId());
    }

    @Test
    public void testUpdateProfile_NotFound() {
        Caregiver updatedCaregiver = caregiver2;

        UpdateProfileDTO updatedProfileDTO = UpdateProfileDTO.builder().build();
        updatedProfileDTO.setEmail(updatedCaregiver.getEmail());
        updatedProfileDTO.setNik(updatedCaregiver.getNik());
        updatedProfileDTO.setAddress(updatedCaregiver.getAddress());
        updatedProfileDTO.setEmail(updatedCaregiver.getEmail());
        updatedProfileDTO.setSpecialty(updatedCaregiver.getSpecialty());
        updatedProfileDTO.setWorkingSchedule(updatedCaregiver.getWorkingSchedule());
        updatedProfileDTO.setType(updatedCaregiver.getType());
        updatedProfileDTO.setPhone(updatedCaregiver.getPhone());

        assertThrows(NoSuchElementException.class, () -> accountService.updateProfile(UUID.randomUUID().toString(), updatedProfileDTO));
    }

    @Test
    public void testDeleteProfile_Found() {
        Caregiver caregiver = caregiver1;
        when(caregiverRepository.existsById(caregiver.getId())).thenReturn(true);

        accountService.deleteProfile(caregiver.getId());

        assertTrue(caregiverRepository.findById(caregiver.getId()).isEmpty());
        verify(caregiverRepository, times(1)).deleteById(caregiver.getId());
    }
}
