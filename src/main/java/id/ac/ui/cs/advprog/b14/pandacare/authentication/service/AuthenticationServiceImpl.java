package id.ac.ui.cs.advprog.b14.pandacare.authentication.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PacilianRepository pacilianRepository;
    private final CaregiverRepository caregiverRepository;
    private final Map<String, User> sessionMap = new HashMap<>(); // Simulasi session

    public AuthenticationServiceImpl(PacilianRepository pacilianRepository, CaregiverRepository caregiverRepository) {
        this.pacilianRepository = pacilianRepository;
        this.caregiverRepository = caregiverRepository;
    }

    @Override
    public User registerPacilian(Pacilian pacilian) {
        pacilian.setId(UUID.randomUUID().toString());
        pacilian.setType(UserType.PACILIAN);
        return pacilianRepository.save(pacilian);
    }

    @Override
    public User registerCaregiver(Caregiver caregiver) {
        caregiver.setId(UUID.randomUUID().toString());
        caregiver.setType(UserType.CAREGIVER);
        return caregiverRepository.save(caregiver);
    }

    @Override
    public User login(String email, String password) {
        Optional<? extends User> userOpt = pacilianRepository.findByEmail(email)
                .map(p -> (User) p)
                .or(() -> caregiverRepository.findByEmail(email).map(c -> (User) c));

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Email tidak terdaftar");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password salah");
        }

        sessionMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void logout(String userId) {
        sessionMap.remove(userId);
    }
}
