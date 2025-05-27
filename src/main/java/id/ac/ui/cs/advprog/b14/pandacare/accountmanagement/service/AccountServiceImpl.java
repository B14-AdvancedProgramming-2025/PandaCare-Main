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
        User user = pacilianRepository.findById(id).map(u -> (User) u)
                .or(() -> caregiverRepository.findById(id).map(u -> (User) u))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));
        System.out.println(user.getId());
        return convertToUserProfileDTO(user);
    }

    public User findUserByEmail(String email) {
        Optional<Pacilian> pacilianOptional = pacilianRepository.findByEmail(email);
        if (pacilianOptional.isPresent()) {
//            System.out.println("Found Pacilian with email: " + email);
//            System.out.println("Pacilian ID: " + pacilianOptional.get().getId());
            return pacilianOptional.get();
        }
        Optional<Caregiver> caregiverOptional = caregiverRepository.findByEmail(email);
        if (caregiverOptional.isPresent()) {
//            System.out.println("Found Caregiver with email: " + email);
            return caregiverOptional.get();
        }
//        System.out.println("No user found with email: " + email);
        return null;
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(String id, UpdateProfileDTO dto) {
        User user = pacilianRepository.findById(id).map(u -> (User) u)
                .or(() -> caregiverRepository.findById(id).map(u -> (User) u))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));

        // NIK and email cannot be changed
        if (dto.getNik() != null && !dto.getNik().equals(user.getNik())) {
            throw new IllegalArgumentException("NIK cannot be updated.");
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("Email cannot be updated.");
        }

        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());

        if (user instanceof Pacilian pacilian) {
            if (dto.getMedicalHistory() != null) {
                pacilian.setMedicalHistory(dto.getMedicalHistory());
            }
            pacilianRepository.save(pacilian);
        } else if (user instanceof Caregiver caregiver) {
            if (dto.getSpecialty() != null) {
                caregiver.setSpecialty(dto.getSpecialty());
            }
            if (dto.getWorkingSchedule() != null) {
                caregiver.setWorkingSchedule(dto.getWorkingSchedule());
            }
            caregiverRepository.save(caregiver);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
        return convertToUserProfileDTO(user);
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

    private UserProfileDTO convertToUserProfileDTO(User user) {
        UserProfileDTO.UserProfileDTOBuilder builder = UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nik(user.getNik())
                .address(user.getAddress())
                .phone(user.getPhone())
                .type(user.getType());

        if (user instanceof Pacilian pacilian) {
//            System.out.println("alala");
            builder.medicalHistory(
                    pacilian.getMedicalHistory() != null ? pacilian.getMedicalHistory() : Collections.emptyList()
            );
            builder.consultationHistory(consultationRepository.findByCaregiverId(pacilian.getId()));
        } else if (user instanceof Caregiver caregiver) {
            builder.specialty(caregiver.getSpecialty());
            builder.workingSchedule(workingScheduleRepository.findByCaregiverId(caregiver.getId()));
//            builder.workingSchedule(
//                    caregiver.getWorkingSchedule() != null ?
//                            caregiver.getWorkingSchedule().stream()
//                                    .map(scheduleString -> WorkingScheduleDTO.builder().status(scheduleString).build())
//                                    .collect(Collectors.toList())
//                            : Collections.emptyList()
//            );
//
            builder.consultationHistory(consultationRepository.findByCaregiverId(caregiver.getId()));
//            System.out.println(consultationRepository.findByCaregiverId(caregiver.getId()).toString());
        }
//        builder.consultationHistory(Collections.emptyList());
        return builder.build();
    }
}