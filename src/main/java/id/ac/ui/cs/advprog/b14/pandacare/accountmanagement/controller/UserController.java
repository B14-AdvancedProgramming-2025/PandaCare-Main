package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/account-management/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search/findByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            // It's good practice to return a more specific DTO rather than the full User entity
            // For now, returning null body for 404 as before
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
}
