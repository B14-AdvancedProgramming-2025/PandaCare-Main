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

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getProfileById(String id) {
        User user = pacilianRepository.findById(id)
                .<User>map(pacilian -> {
                    Hibernate.initialize(pacilian.getMedicalHistory());
                    return pacilian;
                })
                .or(() -> caregiverRepository.findById(id)
                        .map(caregiver -> {
                            return (User) caregiver;
                        }))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        System.out.println("owh " + user.getId());
        return convertToUserProfileDTO(user);
    }

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
            builder.medicalHistory(pacilian.getMedicalHistory());
        } else if (user instanceof Caregiver) {
            Caregiver caregiver = (Caregiver) user;
            builder.specialty(caregiver.getSpecialty());
        }

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
        User userToUpdate = pacilianRepository.findById(id).map(u -> (User) u)
                .or(() -> caregiverRepository.findById(id).map(u -> (User) u))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        userToUpdate.setName(dto.getName());
        userToUpdate.setAddress(dto.getAddress());
        userToUpdate.setPhone(dto.getPhone());

        if (userToUpdate instanceof Pacilian && dto.getMedicalHistory() != null) {
            ((Pacilian) userToUpdate).setMedicalHistory(dto.getMedicalHistory());
        } else if (userToUpdate instanceof Caregiver && dto.getSpecialty() != null) {
            ((Caregiver) userToUpdate).setSpecialty(dto.getSpecialty());
        }

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