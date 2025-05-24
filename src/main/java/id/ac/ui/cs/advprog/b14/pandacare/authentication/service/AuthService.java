package id.ac.ui.cs.advprog.b14.pandacare.authentication.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.config.JwtUtil;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.facade.AuthenticationFacade;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.TokenRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
import id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository.WorkingScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AuthService implements AuthenticationFacade {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                response.put("success", false);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String tokenString = jwtUtil.generateToken(user.getEmail(), user.getRole());

            
            Date expiryDate = jwtUtil.getExpirationDateFromToken(tokenString);
            tokenRepository.deleteExpiredTokens(new Date());

            Token token = new Token(tokenString, expiryDate);
            tokenRepository.save(token);

            response.put("success", true);
            response.put("message", "Login successful");
            Map<String, Object> data = new HashMap<>();
            data.put("token", tokenString);
            data.put("expiryDate", expiryDate.getTime());
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Authentication Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> registerPacilian(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            User existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser != null) {
                response.put("success", false);
                response.put("message", "Email already registered");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            String id = UUID.randomUUID().toString();
            String password = passwordEncoder.encode(request.getPassword());
            Pacilian pacilian = new Pacilian(
                    id,
                    request.getEmail(),
                    password,
                    request.getName(),
                    request.getNik(),
                    request.getAddress(),
                    request.getPhone(),
                    request.getMedicalHistory()
            );
            pacilian.setType(UserType.PACILIAN);

            Pacilian savedUser = userRepository.save(pacilian);

            response.put("success", true);
            response.put("message", "Registration successful");
            Map<String, Object> data = new HashMap<>();
            data.put("email", savedUser.getEmail());
            data.put("name", savedUser.getName());
            data.put("nik", savedUser.getNik());
            data.put("address", savedUser.getAddress());
            data.put("phone", savedUser.getPhone());
            data.put("medicalHistory", savedUser.getMedicalHistory());
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> registerCaregiver(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            User existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser != null) {
                response.put("success", false);
                response.put("message", "Email already registered");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            String id = UUID.randomUUID().toString();
            String password = passwordEncoder.encode(request.getPassword());
            Caregiver caregiver = new Caregiver(
                    id,
                    request.getEmail(),
                    password,
                    request.getName(),
                    request.getNik(),
                    request.getAddress(),
                    request.getPhone(),
                    request.getSpecialty(),
                    new ArrayList<>()
//                    request.getWorkingSchedule()
            );
            caregiver.setType(UserType.CAREGIVER);

            Caregiver savedUser = userRepository.save(caregiver);

            List<WorkingSchedule>  workingSchedule = new ArrayList<>();
            for (WorkingSchedule schedule : request.getWorkingSchedule()) {
                schedule.setCaregiverId(savedUser.getId());
                workingSchedule.add(schedule);
            }
            savedUser.setWorkingSchedule(workingSchedule);
            userRepository.save(savedUser);

            response.put("success", true);
            response.put("message", "Registration successful");
            Map<String, Object> data = new HashMap<>();
            data.put("email", savedUser.getEmail());
            data.put("name", savedUser.getName());
            data.put("nik", savedUser.getNik());
            data.put("address", savedUser.getAddress());
            data.put("phone", savedUser.getPhone());
            data.put("specialty", savedUser.getSpecialty());
            data.put("workingSchedule", savedUser.getWorkingSchedule());
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> logout(String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {
            String tokenString = extractAndValidateToken(authHeader);
            if (tokenString == null) {
                response.put("success", false);
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Token token = tokenRepository.findByToken(tokenString);

            if (token == null) {
                response.put("success", false);
                response.put("message", "Token not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            tokenRepository.deleteByToken(tokenString);
            response.put("success", true);
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String extractAndValidateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String tokenString = authHeader.substring(7);
            if (!jwtUtil.validateToken(tokenString)) {
                return null;
            }

            Token token = tokenRepository.findByToken(tokenString);
            if (token == null || token.isExpired()) {
                return null;
            }
            return tokenString;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> refreshToken(String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {
            String oldToken = extractAndValidateToken(authHeader);
            if (oldToken == null) {
                response.put("success", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String email = jwtUtil.getEmailFromToken(oldToken);
            User user = userRepository.findByEmail(email);

            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Invalidate old token
            tokenRepository.deleteByToken(oldToken);

            // Generate new token
            String newTokenString = jwtUtil.generateToken(email, user.getRole().toUpperCase());
            Date expiryDate = jwtUtil.getExpirationDateFromToken(newTokenString);

            // Save new token
            Token newToken = new Token(newTokenString, expiryDate);
            tokenRepository.save(newToken);

            response.put("success", true);
            response.put("message", "Token refreshed successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("token", newTokenString);
            data.put("expiryDate", expiryDate.getTime());
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Token refresh error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}