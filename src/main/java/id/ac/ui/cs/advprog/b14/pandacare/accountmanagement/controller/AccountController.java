package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UserProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getCurrentUserProfile(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "User not authenticated or token invalid", null));
        }

        String emailFromToken = currentUser.getEmail();

        User userFromService = accountService.findUserByEmail(emailFromToken);

        if (userFromService == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "User details not found via email: " + emailFromToken, null));
        }
//        System.out.println("User found: " + userFromService.getId() + ", Email: " + userFromService.getEmail());

        UserProfileDTO userProfile = accountService.getProfileById(userFromService.getId());
        System.out.println("[AccountController /me] User Profile Retrieved: ID=" + userProfile.getId() + ", Email=" + userProfile.getEmail() + ", Name=" + userProfile.getName() + ", Type=" + userProfile.getType() + "med history=" + userProfile.getMedicalHistory()); // MODIFIED: More detailed logging
        ApiResponse<UserProfileDTO> response = new ApiResponse<>(200, "Current user profile retrieved successfully", userProfile);
//        System.out.println("Response: " + response);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<UserProfileDTO>> getProfile(@PathVariable String id) {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("AUTH: " + auth);
//        System.out.println("Authorities: " + auth.getAuthorities());
//        UserProfileDTO userProfile = accountService.getProfileById(id);
//        ApiResponse<UserProfileDTO> response = new ApiResponse<>(200, "Profile retrieved successfully", userProfile);
//        return ResponseEntity.ok(response);
//    }

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

    @GetMapping("/search/findByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
        System.out.println("yuhu");// Changed return type to ResponseEntity<?>
        User user = accountService.findUserByEmail(email);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
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
