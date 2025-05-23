package id.ac.ui.cs.advprog.b14.pandacare.authentication.facade;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthenticationFacade {
    ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest);
    ResponseEntity<Map<String, Object>> registerPacilian(RegisterRequest registerRequest);
    ResponseEntity<Map<String, Object>> registerCaregiver(RegisterRequest registerRequest);
    ResponseEntity<Map<String, Object>> logout(String authHeader);
    ResponseEntity<Map<String, Object>> refreshToken(String authHeader);
}