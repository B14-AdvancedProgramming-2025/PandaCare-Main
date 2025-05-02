package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceImplTest {

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl();
    }

    @Test
    void testCreateAndGetAccount() {
        Account account = Account.builder()
                .email("test@example.com")
                .password("password")
                .name("John")
                .nik("12345")
                .address("Address")
                .phoneNumber("08123456789")
                .build();

        accountService.createAccount(account);

        Account result = accountService.getAccountById(account.getId());
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testUpdateAccount() {
        Account account = Account.builder()
                .email("old@example.com")
                .password("pass")
                .name("Old Name")
                .nik("54321")
                .address("Old Address")
                .phoneNumber("0812")
                .build();

        accountService.createAccount(account);

        Account updated = Account.builder()
                .id(account.getId())
                .email("new@example.com")
                .password("newpass")
                .name("New Name")
                .nik("54321") // should stay the same
                .address("New Address")
                .phoneNumber("0899")
                .build();

        accountService.updateAccount(updated);
        Account result = accountService.getAccountById(account.getId());

        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void testDeleteAccount() {
        Account account = Account.builder()
                .email("toDelete@example.com")
                .password("pass")
                .name("Delete Me")
                .nik("00000")
                .address("Somewhere")
                .phoneNumber("0812")
                .build();

        accountService.createAccount(account);
        accountService.deleteAccount(account.getId());

        assertThrows(NoSuchElementException.class, () -> {
            accountService.getAccountById(account.getId());
        });
    }
}
