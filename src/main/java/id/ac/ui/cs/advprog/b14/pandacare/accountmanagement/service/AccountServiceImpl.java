package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

//import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.ConsultationDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
//import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.WorkingScheduleDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.ConsultationRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.service.ConsultationService;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository.PacilianRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    private final WorkingScheduleRepository workingScheduleRepository;
    private final ConsultationRepository consultationRepository;



    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getProfileById(String id) {
        User user = pacilianRepository.findById(id)
                .<User>map(pacilian -> {
                    Hibernate.initialize(pacilian.getMedicalHistory()); // Initialize medicalHistory
                    // If Pacilian has a direct consultationHistory field that is lazy and needed:
                    // if (pacilian.getConsultationHistory() != null) Hibernate.initialize(pacilian.getConsultationHistory());
                    return pacilian;
                })
                .or(() -> caregiverRepository.findById(id)
                        .map(caregiver -> {
                            // Initialize lazy collections for Caregiver if needed by convertToUserProfileDTO
                            // if (caregiver.getWorkingSchedule() != null) Hibernate.initialize(caregiver.getWorkingSchedule());
                            // if (caregiver.getConsultationHistory() != null) Hibernate.initialize(caregiver.getConsultationHistory());
                            return (User) caregiver;
                        }))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        System.out.println("owh " + user.getId());
        return convertToUserProfileDTO(user); // convertToUserProfileDTO needs to handle User subtypes
    }

    // This method needs to be implemented to convert User to UserProfileDTO
    // It should check the type of User (Pacilian or Caregiver) and map accordingly.
    private UserProfileDTO convertToUserProfileDTO(User user) {
        UserProfileDTO.UserProfileDTOBuilder builder = UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nik(user.getNik())
                .address(user.getAddress())
                .phone(user.getPhone())
                .type(user.getType());

        if (user instanceof Pacilian) {
            Pacilian pacilian = (Pacilian) user;
            builder.medicalHistory(pacilian.getMedicalHistory()); // This is now safe to access
            // If UserProfileDTO expects consultation history directly from Pacilian:
            // builder.consultationHistory(pacilian.getConsultationHistory());
        } else if (user instanceof Caregiver) {
            Caregiver caregiver = (Caregiver) user;
            builder.specialty(caregiver.getSpecialty()); // Assuming Caregiver has getSpecialty()
            // If UserProfileDTO expects working schedule and consultation history from Caregiver:
            // builder.workingSchedule(caregiver.getWorkingSchedule());
            // builder.consultationHistory(caregiver.getConsultationHistory());
        }
        // Note: The current UserProfileDTO has a single consultationHistory field.
        // You'll need to decide how to populate this. Is it common to both, or specific?
        // If it's fetched separately or common, it might be handled outside the if/else.

        return builder.build();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        Optional<Pacilian> pacilianOptional = pacilianRepository.findByEmail(email);
        if (pacilianOptional.isPresent()) {
            return pacilianOptional.get();
        }
        Optional<Caregiver> caregiverOptional = caregiverRepository.findByEmail(email);
        if (caregiverOptional.isPresent()) {
            return caregiverOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(String id, UpdateProfileDTO dto) {
        // Implementation for updateProfile - fetch user, update, save, convert to DTO
        User userToUpdate = pacilianRepository.findById(id).map(u -> (User) u)
                .or(() -> caregiverRepository.findById(id).map(u -> (User) u))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        // Update common fields
        userToUpdate.setName(dto.getName());
        userToUpdate.setAddress(dto.getAddress());
        userToUpdate.setPhone(dto.getPhone());
        // NIK and email are typically not updatable or handled with care

        if (userToUpdate instanceof Pacilian && dto.getMedicalHistory() != null) {
            ((Pacilian) userToUpdate).setMedicalHistory(dto.getMedicalHistory());
        } else if (userToUpdate instanceof Caregiver && dto.getSpecialty() != null) {
            ((Caregiver) userToUpdate).setSpecialty(dto.getSpecialty());
            // Note: workingSchedule updates would be more complex, likely through a separate endpoint/service
        }

        // Save the updated user - repository.save() is needed if changes are made
        if (userToUpdate instanceof Pacilian) {
            pacilianRepository.save((Pacilian) userToUpdate);
        } else if (userToUpdate instanceof Caregiver) {
            caregiverRepository.save((Caregiver) userToUpdate);
        }

        return convertToUserProfileDTO(userToUpdate);
    }

    @Override
    @Async
    @Transactional
    public void deleteProfile(String id) {
        if (pacilianRepository.existsById(id)) {
            pacilianRepository.deleteById(id);
        } else if (caregiverRepository.existsById(id)) {
            caregiverRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User not found with ID: " + id);
        }
    }
}