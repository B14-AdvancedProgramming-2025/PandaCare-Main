package id.ac.ui.cs.advprog.b14.pandacare.authentication.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.LoginRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.facade.AuthenticationFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationFacade authFacade;

    public AuthController(AuthenticationFacade authFacade) {
        this.authFacade = authFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authFacade.login(loginRequest);
    }

    @PostMapping("/register/pacilian")
    public ResponseEntity<Map<String, Object>> registerPacilian(@Valid @RequestBody RegisterRequest registerRequest) {
        return authFacade.registerPacilian(registerRequest);
    }

    @PostMapping("/register/caregiver")
    public ResponseEntity<Map<String, Object>> registerCaregiver(@Valid @RequestBody RegisterRequest registerRequest) {
        return authFacade.registerCaregiver(registerRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        return authFacade.logout(authHeader);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        return authFacade.refreshToken(authHeader);
    }
}