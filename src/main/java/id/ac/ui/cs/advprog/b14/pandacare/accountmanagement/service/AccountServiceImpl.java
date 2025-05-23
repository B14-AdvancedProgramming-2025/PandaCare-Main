package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.PacilianRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    public UserDTO getProfileById(String id) {
        User user = pacilianRepository.findById(id).map(u -> (User) u)
                .or(() -> caregiverRepository.findById(id).map(u -> (User) u))
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateProfile(String id, UpdateProfileDTO dto) {

        // Ambil data user berdasarkan tipe
        UserType type = dto.getType();
        User savedUser;
        System.out.println("Received user type: " + dto.getType());


        if (type == UserType.PACILIAN) {
            Pacilian existing = pacilianRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Pacilian not found"));

            if (dto.getEmail() != null && !existing.getEmail().equals(dto.getEmail())) {
                throw new IllegalArgumentException("Email cannot be updated");
            }

            // Update field umum
            existing.setName(dto.getName());
            existing.setAddress(dto.getAddress());
            existing.setPhone(dto.getPhone());

            // Update field khusus Pacilian
            if (dto.getMedicalHistory() != null) {
                existing.setMedicalHistory(dto.getMedicalHistory());
            }

            savedUser = pacilianRepository.save(existing);

        } else if (type == UserType.CAREGIVER) {
            Caregiver existing = caregiverRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Caregiver not found"));

            if (dto.getEmail() != null && !existing.getEmail().equals(dto.getEmail())) {
                throw new IllegalArgumentException("Email cannot be updated");
            }


            // Update field umum
            existing.setName(dto.getName());
            existing.setAddress(dto.getAddress());
            existing.setPhone(dto.getPhone());

            // Update field khusus Caregiver
            if (dto.getSpecialty() != null) {
                existing.setSpecialty(dto.getSpecialty());
            }
            if (dto.getWorkingSchedule() != null) {
                existing.setWorkingSchedule(dto.getWorkingSchedule());
            }

            savedUser = caregiverRepository.save(existing);

        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        return convertToDTO(savedUser);
    }

    @Override
    @Async
    public void deleteProfile(String id) {
        if (pacilianRepository.existsById(id)) {
            pacilianRepository.deleteById(id);
        } else if (caregiverRepository.existsById(id)) {
            caregiverRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User not found");
        }
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getType(),
                user.getAddress(),
                user.getPhone()
        );
    }
}
