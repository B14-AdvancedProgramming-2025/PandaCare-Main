package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.service.UserService;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transaction;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    private WalletController walletController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletController = new WalletController(walletService, userService);
    }

    @Test
    void createWallet_Success() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        Wallet wallet = new Wallet(user);
        wallet.setId(1L);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);
        when(walletService.createWalletForUser(user)).thenReturn(wallet);

        // Act
        ResponseEntity<Wallet> response = walletController.createWallet(authentication);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(wallet, response.getBody());
        verify(userService).findByUsername(username);
        verify(walletService).createWalletForUser(user);
    }

    @Test
    void createWallet_Exception() {
        // Arrange
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername(anyString())).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> walletController.createWallet(authentication));
        verify(walletService, never()).createWalletForUser(any());
    }

    @Test
    void getBalance_Success() {
        // Arrange
        Long walletId = 1L;
        Double expectedBalance = 100.0;
        when(walletService.getBalance(walletId)).thenReturn(expectedBalance);

        // Act
        ResponseEntity<Map<String, Double>> response = walletController.getBalance(walletId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBalance, response.getBody().get("balance"));
    }

    @Test
    void getBalance_WalletNotFound() {
        // Arrange
        Long walletId = 999L;
        when(walletService.getBalance(walletId)).thenThrow(new EntityNotFoundException("Wallet not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> walletController.getBalance(walletId));
    }

    @Test
    void getTransactionHistory_Success() {
        // Arrange
        Long walletId = 1L;
        List<Transaction> expectedTransactions = Arrays.asList(new Transaction(), new Transaction());
        when(walletService.getTransactionHistory(walletId)).thenReturn(expectedTransactions);

        // Act
        ResponseEntity<List<Transaction>> response = walletController.getTransactionHistory(walletId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTransactions, response.getBody());
    }

    @Test
    void getTransactionHistory_WalletNotFound() {
        // Arrange
        Long walletId = 999L;
        when(walletService.getTransactionHistory(walletId)).thenThrow(new EntityNotFoundException("Wallet not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> walletController.getTransactionHistory(walletId));
    }
}