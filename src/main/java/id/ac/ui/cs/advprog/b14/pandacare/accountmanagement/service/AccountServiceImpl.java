package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.ConsultationDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.CaregiverRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.PacilianRepository;
// Import Consultation model and repository (assuming they exist)
// import id.ac.ui.cs.advprog.b14.pandacare.consultation.model.Consultation;
// import id.ac.ui.cs.advprog.b14.pandacare.consultation.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    // private final ConsultationRepository consultationRepository; // Assuming this exists

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getProfileById(String id) {
        User user = pacilianRepository.findById(id).map(u -> (User) u)
                .or(() -> caregiverRepository.findById(id).map(u -> (User) u))
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));
        return convertToUserProfileDTO(user);
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
    @Transactional // Add @Transactional
    public void deleteProfile(String id) {
        if (pacilianRepository.existsById(id)) {
            // Consider deleting related data like consultations before deleting the user
            pacilianRepository.deleteById(id);
        } else if (caregiverRepository.existsById(id)) {
            // Consider deleting related data like consultations before deleting the user
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
            builder.medicalHistory(
                    pacilian.getMedicalHistory() != null ? pacilian.getMedicalHistory() : Collections.emptyList()
            );
        } else if (user instanceof Caregiver caregiver) {
            builder.specialty(caregiver.getSpecialty());
            builder.workingSchedule(
                    caregiver.getWorkingSchedule() != null ? caregiver.getWorkingSchedule() : Collections.emptyList()
            );
        }

        builder.consultationHistory(Collections.emptyList()); // Belum ada implementasi real
        return builder.build();
    }


    // Placeholder for consultation history logic
    // Uncomment and implement when Consultation model and repository are available
    /*
    private List<ConsultationDTO> getConsultationHistory(User user) {
        List<Consultation> consultations;
        if (user instanceof Pacilian) {
            consultations = consultationRepository.findByPacilianIdOrderByConsultationTimeDesc(user.getId());
        } else if (user instanceof Caregiver) {
            consultations = consultationRepository.findByCaregiverIdOrderByConsultationTimeDesc(user.getId());
        } else {
            return Collections.emptyList();
        }

        return consultations.stream()
                .map(consultation -> ConsultationDTO.builder()
                        .consultationTime(consultation.getConsultationTime())
                        .partnerName(user instanceof Pacilian ? consultation.getCaregiver().getName() : consultation.getPacilian().getName())
                        .notes(consultation.getNotes())
                        .build())
                .collect(Collectors.toList());
    }
    */
}
