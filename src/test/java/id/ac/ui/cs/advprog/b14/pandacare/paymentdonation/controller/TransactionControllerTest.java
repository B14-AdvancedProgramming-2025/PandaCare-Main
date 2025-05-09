package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.PaymentRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransferRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private WalletService walletService;

    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(walletService);
    }

    @Test
    void processPayment_Success() {
        // Arrange
        Long walletId = 1L;
        Double amount = 100.0;
        String merchantId = "MERCHANT123";
        String invoiceNumber = "INV-001";

        PaymentRequest request = new PaymentRequest(walletId, amount, "Test payment", merchantId, invoiceNumber);
        when(walletService.makePayment(eq(walletId), eq(amount), anyString())).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Object>> response = transactionController.processPayment(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(amount, response.getBody().get("amount"));
        assertEquals(merchantId, response.getBody().get("merchantId"));
        assertEquals(invoiceNumber, response.getBody().get("invoiceNumber"));
        verify(walletService).makePayment(eq(walletId), eq(amount), anyString());
    }

    @Test
    void processPayment_InsufficientFunds() {
        // Arrange
        PaymentRequest request = new PaymentRequest(1L, 500.0, "Test payment", "MERCHANT123", "INV-001");
        when(walletService.makePayment(eq(1L), eq(500.0), anyString())).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = transactionController.processPayment(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Insufficient funds for payment", response.getBody().get("message"));
    }

    @Test
    void processPayment_WalletNotFound() {
        // Arrange
        PaymentRequest request = new PaymentRequest(999L, 100.0, "Test payment", "MERCHANT123", "INV-001");
        when(walletService.makePayment(eq(999L), eq(100.0), anyString()))
                .thenThrow(new EntityNotFoundException("Wallet not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> transactionController.processPayment(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void processTransfer_Success() {
        // Arrange
        Long walletId = 1L;
        Long recipientWalletId = 2L;
        Double amount = 100.0;
        String reference = "REF-001";

        TransferRequest request = new TransferRequest(walletId, amount, "Test transfer", recipientWalletId, reference);
        when(walletService.Transfer(walletId, recipientWalletId, amount)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Object>> response = transactionController.processTransfer(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(amount, response.getBody().get("amount"));
        assertEquals(recipientWalletId, response.getBody().get("recipientWalletId"));
        assertEquals(reference, response.getBody().get("reference"));
        verify(walletService).Transfer(walletId, recipientWalletId, amount);
    }

    @Test
    void processTransfer_InsufficientFunds() {
        // Arrange
        TransferRequest request = new TransferRequest(1L, 500.0, "Test transfer", 2L, "REF-001");
        when(walletService.Transfer(1L, 2L, 500.0)).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = transactionController.processTransfer(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Insufficient funds for transfer", response.getBody().get("message"));
    }

    @Test
    void processTransfer_WalletNotFound() {
        // Arrange
        TransferRequest request = new TransferRequest(999L, 100.0, "Test transfer", 2L, "REF-001");
        when(walletService.Transfer(999L, 2L, 100.0))
                .thenThrow(new EntityNotFoundException("Sender wallet not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> transactionController.processTransfer(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}