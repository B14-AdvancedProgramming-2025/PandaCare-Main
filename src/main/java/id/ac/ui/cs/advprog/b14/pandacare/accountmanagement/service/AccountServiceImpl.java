package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.PacilianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    public User getProfileById(String id) {
        return pacilianRepository.findById(id).map(user -> (User) user)
                .or(() -> caregiverRepository.findById(id).map(user -> (User) user))
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public User updateProfile(String id, User updatedUser) {
        if (updatedUser instanceof Pacilian pacilianUpdate) {
            Pacilian existing = pacilianRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Pacilian not found"));

            if (!existing.getNik().equals(updatedUser.getNik())) {
                throw new IllegalArgumentException("NIK cannot be updated");
            }

            updateCommonFields(existing, updatedUser);
            existing.setMedicalHistory(pacilianUpdate.getMedicalHistory());
            return pacilianRepository.save(existing);

        } else if (updatedUser instanceof Caregiver caregiverUpdate) {
            Caregiver existing = caregiverRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Caregiver not found"));

            if (!existing.getNik().equals(updatedUser.getNik())) {
                throw new IllegalArgumentException("NIK cannot be updated");
            }

            updateCommonFields(existing, updatedUser);
            existing.setSpecialty(caregiverUpdate.getSpecialty());
            existing.setWorkingSchedule(caregiverUpdate.getWorkingSchedule());
            return caregiverRepository.save(existing);
        }

        throw new IllegalArgumentException("Invalid user type");
    }


    @Override
    public void deleteProfile(String id) {
        if (pacilianRepository.existsById(id)) {
            pacilianRepository.deleteById(id);
        } else if (caregiverRepository.existsById(id)) {
            caregiverRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User not found");
        }
    }

    private void updateCommonFields(User existing, User updated) {
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        existing.setPhone(updated.getPhone());
        existing.setPassword(updated.getPassword());
    }
}
