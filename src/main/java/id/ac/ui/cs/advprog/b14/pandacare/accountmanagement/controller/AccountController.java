package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller.ApiResponse;
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
    public ResponseEntity<ApiResponse<User>> getProfile(@PathVariable String id) {
        User user = accountService.getProfileById(id);
        ApiResponse<User> response = new ApiResponse<>(200, "Profile retrieved successfully", user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateProfile(@PathVariable String id, @RequestBody User user) {
        User updatedUser = accountService.updateProfile(id, user);
        ApiResponse<User> response = new ApiResponse<>(200, "Profile updated successfully", updatedUser);
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
}

