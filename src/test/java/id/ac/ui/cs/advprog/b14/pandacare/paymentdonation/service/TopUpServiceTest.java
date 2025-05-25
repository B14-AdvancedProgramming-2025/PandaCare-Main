package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopUpServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TopUpService topUpService;

    private User user;
    private Wallet wallet;
    private TopUpRequest bankTransferRequest;
    private TopUpRequest creditCardRequest;

    @BeforeEach
    void setUp() {
        user = new Pacilian(
                "pacil-123",
                "test@example.com",
                "password123",
                "Test User",
                "1234567890123456",
                "123 Test Street",
                "08123456789",
                Arrays.asList("None")
        );

        wallet = new Wallet(user);
        wallet.setBalance(100.0);
        wallet.setTransactions(new ArrayList<>());

        bankTransferRequest = new TopUpRequest();
        bankTransferRequest.setAmount(50.0);
        bankTransferRequest.setMethod("BANK_TRANSFER");
        bankTransferRequest.setBankName("Test Bank");
        bankTransferRequest.setAccountNumber("123456789");

        creditCardRequest = new TopUpRequest();
        creditCardRequest.setAmount(100.0);
        creditCardRequest.setMethod("CREDIT_CARD");
        creditCardRequest.setCardNumber("4111111111111111");
        creditCardRequest.setCvv("123");
        creditCardRequest.setExpiryDate("12/25");
        creditCardRequest.setCardholderName("Test User");
    }

    @Test
    void testProcessTopUpWithBankTransferSuccess() {
        when(walletRepository.findByUser(user)).thenReturn(wallet);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, bankTransferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Topped-Up successfully", body.get("message"));

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertEquals(50.0, data.get("amount"));
        assertEquals(150.0, data.get("balance"));
        assertTrue(((String) data.get("provider")).contains("Bank Transfer"));

        verify(walletRepository).findByUser(user);
        verify(walletRepository).save(wallet);
        assertEquals(150.0, wallet.getBalance());
        assertEquals(1, wallet.getTransactions().size());

        TopUp topUp = (TopUp) wallet.getTransactions().get(0);
        assertEquals(50.0, topUp.getAmount());
        assertEquals(TransactionType.TOPUP, topUp.getType());
    }

    @Test
    void testProcessTopUpWithCreditCardSuccess() {
        when(walletRepository.findByUser(user)).thenReturn(wallet);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, creditCardRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertEquals(100.0, data.get("amount"));
        assertEquals(200.0, data.get("balance"));
        assertTrue(((String) data.get("provider")).contains("Credit Card"));

        verify(walletRepository).save(wallet);
        assertEquals(200.0, wallet.getBalance());
    }

    @Test
    void testProcessTopUpWithNegativeAmount() {
        TopUpRequest request = new TopUpRequest();
        request.setAmount(-50.0);
        request.setMethod("BANK_TRANSFER");

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Amount must be greater than zero", body.get("message"));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void testProcessTopUpWithWalletNotFound() {
        when(walletRepository.findByUser(user)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, bankTransferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Wallet not found", body.get("message"));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void testProcessTopUpWithInvalidMethod() {
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        TopUpRequest request = new TopUpRequest();
        request.setAmount(50.0);
        request.setMethod("INVALID_METHOD");

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Invalid method", body.get("message"));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void testProcessTopUpWithMissingBankDetails() {
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        TopUpRequest request = new TopUpRequest();
        request.setAmount(50.0);
        request.setMethod("BANK_TRANSFER");
        // Missing bank name

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Bank name is required", body.get("message"));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void testProcessTopUpWithInvalidCardDetails() {
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        TopUpRequest request = new TopUpRequest();
        request.setAmount(50.0);
        request.setMethod("CREDIT_CARD");
        request.setCardNumber("invalid"); // Invalid card number

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Invalid card number", body.get("message"));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void testProcessTopUpWithExpiredCard() {
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        TopUpRequest request = new TopUpRequest();
        request.setAmount(50.0);
        request.setMethod("CREDIT_CARD");
        request.setCardNumber("4111111111111111");
        request.setCvv("123");
        request.setExpiryDate("01/20"); // Expired date
        request.setCardholderName("Test User");

        ResponseEntity<Map<String, Object>> response = topUpService.processTopUp(user, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Card is expired", body.get("message"));

        verify(walletRepository, never()).save(any());
    }
}