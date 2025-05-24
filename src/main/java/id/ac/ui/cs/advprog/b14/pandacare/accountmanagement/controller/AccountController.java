package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getProfile(@PathVariable String id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH: " + auth);
        System.out.println("Authorities: " + auth.getAuthorities());
        UserProfileDTO userProfile = accountService.getProfileById(id);
        ApiResponse<UserProfileDTO> response = new ApiResponse<>(200, "Profile retrieved successfully", userProfile);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateProfile(@PathVariable String id, @RequestBody UpdateProfileDTO dto) {
        UserProfileDTO updatedUserProfile = accountService.updateProfile(id, dto);
        ApiResponse<UserProfileDTO> response = new ApiResponse<>(200, "Profile updated successfully", updatedUserProfile);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProfile(@PathVariable String id) {
        accountService.deleteProfile(id);
        ApiResponse<String> response = new ApiResponse<>(200, "Profile deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(NoSuchElementException ex) {
        ApiResponse<String> response = new ApiResponse<>(404, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<String> response = new ApiResponse<>(400, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

