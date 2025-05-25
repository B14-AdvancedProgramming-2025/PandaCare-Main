package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Import DeleteMapping
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap; // Import HashMap

@RestController
@RequestMapping("/api/account-management/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search/findByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) { // Changed return type to ResponseEntity<?>
        User user = userService.findUserByEmail(email);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            // Anda bisa menambahkan field lain yang aman dan dibutuhkan, misal type atau name jika perlu
            // response.put(\"email\", user.getEmail());
            // response.put(\"name\", user.getName());
            // response.put(\"type\", user.getType().name());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    @GetMapping("/{userId}") // New endpoint
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        User user = userService.findUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable String userId, @RequestBody Map<String, Object> updates) {
        User existingUser = userService.findUserById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            User updatedUser = userService.updateUserProfile(userId, existingUser.getType().name(), updates);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Or a more specific error response
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content on successful deletion
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // HTTP 404 if user not found
        } catch (Exception e) {
            // Log the exception for server-side analysis
            // logger.error(\"Error deleting user with ID: {}\", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // HTTP 500 for other errors
        }
    }
}
