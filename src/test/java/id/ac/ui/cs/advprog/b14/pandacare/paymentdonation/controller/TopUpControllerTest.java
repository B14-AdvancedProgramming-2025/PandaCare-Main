package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.TopUpService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopUpControllerTest {

    @Mock
    private TopUpService topUpService;

    private TopUpController topUpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        topUpController = new TopUpController(topUpService);
    }

    @Test
    void processTopUp_Success() {
        // Arrange
        Long walletId = 1L;
        Double amount = 100.0;
        Map<String, String> paymentDetails = new HashMap<>();
        paymentDetails.put("provider", "CREDIT_CARD");

        TopUpRequest request = new TopUpRequest(walletId, amount, "Top-up with Credit Card", paymentDetails);
        when(topUpService.topUp(request)).thenReturn(true);

        // Act
        ResponseEntity<Map<String, Object>> response = topUpController.processTopUp(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(amount, response.getBody().get("amount"));
        verify(topUpService).topUp(request);
    }

    @Test
    void processTopUp_Failure() {
        // Arrange
        TopUpRequest request = new TopUpRequest(1L, 100.0, "Failed Top-up", Map.of("provider", "BANK_TRANSFER"));
        when(topUpService.topUp(request)).thenReturn(false);

        // Act
        ResponseEntity<Map<String, Object>> response = topUpController.processTopUp(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Failed to process top-up", response.getBody().get("message"));
    }

    @Test
    void processTopUp_InvalidArgument() {
        // Arrange
        TopUpRequest request = new TopUpRequest(1L, 100.0, "Invalid Top-up", new HashMap<>());
        when(topUpService.topUp(request)).thenThrow(new IllegalArgumentException("Provider not specified"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> topUpController.processTopUp(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void processTopUp_EntityNotFound() {
        // Arrange
        TopUpRequest request = new TopUpRequest(999L, 100.0, "Wallet Not Found", Map.of("provider", "CREDIT_CARD"));
        when(topUpService.topUp(request)).thenThrow(new EntityNotFoundException("Wallet not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> topUpController.processTopUp(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}