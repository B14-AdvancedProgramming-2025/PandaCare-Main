package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    private AccountRepository accountRepository;
    private AccountServiceImpl accountService;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepository);

        testAccount = Account.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .nik("1234567890")
                .address("Some Street")
                .phoneNumber("081234567890")
                .userType(UserType.PACILIAN)
                .medicalHistory(List.of("Diabetes", "Asthma"))
                .specialty(null) // karena ini PACILIAN, bukan Caregiver
                .workingSchedules(null)
                .build();
    }

    @Test
    void testGetAccountById_success() {
        when(accountRepository.findById(testAccount.getId()))
                .thenReturn(Optional.of(testAccount));

        Account result = accountService.getAccountById(testAccount.getId());

        assertNotNull(result);
        assertEquals(testAccount.getEmail(), result.getEmail());
        verify(accountRepository, times(1)).findById(testAccount.getId());
    }

    @Test
    void testGetAccountById_notFound() {
        UUID randomId = UUID.randomUUID();
        when(accountRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> accountService.getAccountById(randomId)
        );

        verify(accountRepository, times(1)).findById(randomId);
    }

    @Test
    void testUpdateAccount_success() {
        when(accountRepository.existsById(testAccount.getId())).thenReturn(true);
        when(accountRepository.save(testAccount)).thenReturn(testAccount);

        Account result = accountService.updateAccount(testAccount);

        assertNotNull(result);
        assertEquals(testAccount.getId(), result.getId());
        verify(accountRepository, times(1)).existsById(testAccount.getId());
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void testUpdateAccount_notFound() {
        when(accountRepository.existsById(testAccount.getId())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> accountService.updateAccount(testAccount));
        verify(accountRepository, times(1)).existsById(testAccount.getId());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testDeleteAccount_success() {
        when(accountRepository.existsById(testAccount.getId())).thenReturn(true);
        doNothing().when(accountRepository).deleteById(testAccount.getId());

        assertDoesNotThrow(() -> accountService.deleteAccount(testAccount.getId()));
        verify(accountRepository, times(1)).existsById(testAccount.getId());
        verify(accountRepository, times(1)).deleteById(testAccount.getId());
    }

    @Test
    void testDeleteAccount_notFound() {
        when(accountRepository.existsById(testAccount.getId())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> accountService.deleteAccount(testAccount.getId()));
        verify(accountRepository, times(1)).existsById(testAccount.getId());
        verify(accountRepository, never()).deleteById(any());
    }
}
