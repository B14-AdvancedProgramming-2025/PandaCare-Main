package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto.UpdateProfileDTO;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller.ApiResponse;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(@PathVariable String id) {
        UserDTO user = accountService.getProfileById(id);
        ApiResponse<UserDTO> response = new ApiResponse<>(200, "Profile retrieved successfully", user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable String id, @RequestBody UpdateProfileDTO dto) {
        UserDTO updatedUser = accountService.updateProfile(id, dto);
        return ResponseEntity.ok(updatedUser);
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
}

