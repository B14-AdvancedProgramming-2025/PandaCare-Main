//package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;
//
//import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
//import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/accounts")
//public class AccountController {
//
//    private final AccountService accountService;
//
//    public AccountController(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
//        Account account = accountService.getAccountById(id);
//        return ResponseEntity.ok(account);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Account> updateAccount(@PathVariable UUID id, @RequestBody Account account) {
//        account.setId(id);
//        Account updatedAccount = accountService.updateAccount(account);
//        return ResponseEntity.ok(updatedAccount);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
//        accountService.deleteAccount(id);
//        return ResponseEntity.noContent().build();
//    }
//}
package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service.AccountService;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getProfileById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.ok(accountService.updateProfile(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id) {
        accountService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
