package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.TransactionRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.TopUpStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopUpServiceTest {

    @Mock
    private User user;

    @Mock
    private TopUpStrategy creditCardStrategy;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private TopUpService topUpService;

    @BeforeEach
    void setUp() {
        when(creditCardStrategy.getProviderName()).thenReturn("CREDIT_CARD");

        topUpService = new TopUpService(
                List.of(creditCardStrategy),
                walletRepository,
                transactionRepository
        );
    }

    @Test
    void topUp_WhenSuccessful_ShouldReturnTrueAndSaveTransaction() {
        // Arrange
        Long walletId = 1L;
        Double amount = 100.0;

        Wallet wallet = new Wallet(user);
        wallet.setId(walletId);
        wallet.setBalance(50.0);

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("provider", "CREDIT_CARD");
        paymentDetails.put("cardNumber", "1234567890123456");

        TopUpRequest request = new TopUpRequest(walletId, amount, "Top-up with Credit Card", paymentDetails);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(creditCardStrategy.processTopUp(wallet, request)).thenReturn(true);

        // Act
        boolean result = topUpService.topUp(request);

        // Assert
        assertTrue(result);

        // Verify strategy was called
        verify(creditCardStrategy).processTopUp(wallet, request);

        // Verify transaction was created and saved
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(wallet, savedTransaction.getWallet());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(TransactionType.TOPUP, savedTransaction.getType());
        assertEquals("Top-up via CREDIT_CARD", savedTransaction.getDescription());
        assertEquals("CREDIT_CARD", savedTransaction.getProvider());

        // Verify wallet was saved
        verify(walletRepository).save(wallet);
    }

    @Test
    void topUp_WhenProviderNotSpecified_ShouldThrowException() {
        // Arrange
        Map<String, String> paymentDetails = new HashMap<>();
        // Missing provider

        TopUpRequest request = new TopUpRequest(1L, 100.0, "Invalid top-up", paymentDetails);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> topUpService.topUp(request)
        );

        assertEquals("Provider not specified in payment gateway details", exception.getMessage());

        // Verify repository methods were not called
        verifyNoInteractions(walletRepository, transactionRepository);
    }

    @Test
    void topUp_WhenUnsupportedProvider_ShouldThrowException() {
        // Arrange
        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("provider", "UNSUPPORTED_PROVIDER");

        TopUpRequest request = new TopUpRequest(1L, 100.0, "Unsupported top-up", paymentDetails);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> topUpService.topUp(request)
        );

        assertEquals("Unsupported top-up method: UNSUPPORTED_PROVIDER", exception.getMessage());

        // Verify repository methods were not called
        verifyNoInteractions(walletRepository, transactionRepository);
    }

    @Test
    void topUp_WhenWalletNotFound_ShouldThrowException() {
        // Arrange
        Long walletId = 999L;

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("provider", "CREDIT_CARD");

        TopUpRequest request = new TopUpRequest(walletId, 100.0, "Wallet not found", paymentDetails);

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> topUpService.topUp(request)
        );

        assertEquals("Wallet not found", exception.getMessage());

        // Verify no transaction was saved
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void topUp_WhenStrategyFails_ShouldReturnFalseWithoutSavingTransaction() {
        // Arrange
        Long walletId = 1L;

        Wallet wallet = new Wallet(user);
        wallet.setId(walletId);

        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("provider", "CREDIT_CARD");

        TopUpRequest request = new TopUpRequest(walletId, 100.0, "Failed top-up", paymentDetails);

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(creditCardStrategy.processTopUp(wallet, request)).thenReturn(false);

        // Act
        boolean result = topUpService.topUp(request);

        // Assert
        assertFalse(result);

        // Verify no transaction was saved
        verifyNoInteractions(transactionRepository);
        verify(walletRepository, never()).save(any());
    }
}