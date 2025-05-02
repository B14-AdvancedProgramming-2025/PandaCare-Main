package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceImplTest {

    private AccountServiceImpl accountService;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl();
        testAccount = Account.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .name("John")
                .nik("12345")
                .address("Address")
                .phoneNumber("08123456789")
                .build();

        AccountServiceImpl.insertTestAccount(testAccount);
    }

    @Test
    void testGetAccountById() {
        Account result = accountService.getAccountById(testAccount.getId().toString());
        assertNotNull(result);
        assertEquals(testAccount.getEmail(), result.getEmail());
    }

    @Test
    void testUpdateAccount() {
        testAccount.setName("Updated Name");
        Account updated = accountService.updateAccount(testAccount);
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteAccount() {
        accountService.deleteAccount(testAccount.getId().toString());
        assertNull(accountService.getAccountById(testAccount.getId().toString()));
    }

    @Test
    void testDeleteNonExistingAccountThrowsException() {
        String fakeId = UUID.randomUUID().toString();
        assertThrows(IllegalArgumentException.class, () -> accountService.deleteAccount(fakeId));
    }

    @Test
    void testUpdateNonExistingAccountThrowsException() {
        Account nonExisting = Account.builder()
                .id(UUID.randomUUID())
                .email("noone@example.com")
                .build();
        assertThrows(IllegalArgumentException.class, () -> accountService.updateAccount(nonExisting));
    }
}
