package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TransferRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transfer;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WalletService walletService;

    private User sender;
    private User receiver;
    private Wallet senderWallet;
    private Wallet receiverWallet;
    private TransferRequest transferRequest;

    @BeforeEach
    void setUp() {
        sender = new Pacilian(
                "pacil-123",
                "sender@example.com",
                "password123",
                "Sender User",
                "1234567890123456",
                "123 Sender Street",
                "08123456789",
                Arrays.asList("None")
        );

        receiver = new Caregiver(
                "care-456",
                "receiver@example.com",
                "password456",
                "Receiver User",
                "6543210987654321",
                "456 Receiver Street",
                "08987654321",
                "General Care",
                Arrays.asList("Monday 9-5", "Wednesday 9-5", "Friday 9-5")
        );

        senderWallet = new Wallet(sender);
        senderWallet.setId(1L);
        senderWallet.setBalance(500.0);
        senderWallet.setTransactions(new ArrayList<>());

        receiverWallet = new Wallet(receiver);
        receiverWallet.setId(2L);
        receiverWallet.setBalance(200.0);
        receiverWallet.setTransactions(new ArrayList<>());

        transferRequest = new TransferRequest();
        transferRequest.setReceiverEmail("receiver@example.com");
        transferRequest.setAmount(100.0);
        transferRequest.setNote("Test transfer");
    }

    @Test
    void getBalanceSuccess() {
        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);

        ResponseEntity<Map<String, Object>> response = walletService.getBalance(sender);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Balance retrieved successfully", body.get("message"));

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertEquals(500.0, data.get("balance"));

        verify(walletRepository).findByUser(sender);
    }

    @Test
    void getBalanceWalletNotFound() {
        when(walletRepository.findByUser(sender)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = walletService.getBalance(sender);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Wallet not found", body.get("message"));

        verify(walletRepository).findByUser(sender);
    }

    @Test
    void transferFundsSuccess() {
        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(receiver);
        when(walletRepository.findByUser(receiver)).thenReturn(receiverWallet);
        when(walletRepository.save(any(Wallet.class))).thenReturn(senderWallet, receiverWallet);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Transfer successful", body.get("message"));

        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertEquals(sender.getName(), data.get("sender"));
        assertEquals(400.0, data.get("senderBalance"));
        assertEquals(receiver.getName(), data.get("receiver"));
        assertEquals(300.0, data.get("receiverBalance"));
        assertEquals(100.0, data.get("amount"));
        assertEquals("Test transfer", data.get("note"));

        verify(walletRepository).findByUser(sender);
        verify(userRepository).findByEmail("receiver@example.com");
        verify(walletRepository).findByUser(receiver);
        verify(walletRepository, times(2)).save(any(Wallet.class));

        assertEquals(400.0, senderWallet.getBalance());
        assertEquals(300.0, receiverWallet.getBalance());
        assertEquals(1, senderWallet.getTransactions().size());
        assertEquals(1, receiverWallet.getTransactions().size());
    }

    @Test
    void transferFundsSenderWalletNotFound() {
        when(walletRepository.findByUser(sender)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Sender wallet not found", body.get("message"));

        verify(walletRepository).findByUser(sender);
        verify(userRepository, never()).findByEmail(anyString());
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void transferFundsReceiverNotFound() {
        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Receiver not found", body.get("message"));

        verify(walletRepository).findByUser(sender);
        verify(userRepository).findByEmail("receiver@example.com");
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void transferFundsReceiverWalletNotFound() {
        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(receiver);
        when(walletRepository.findByUser(receiver)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Receiver wallet not found", body.get("message"));

        verify(walletRepository).findByUser(sender);
        verify(userRepository).findByEmail("receiver@example.com");
        verify(walletRepository).findByUser(receiver);
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void transferFundsSameWallet() {
        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(sender);
        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Cannot transfer to the same wallet", body.get("message"));

        verify(walletRepository, atMost(2)).findByUser(sender);
        verify(userRepository).findByEmail("receiver@example.com");
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void transferFundsNegativeAmount() {
        transferRequest.setAmount(-50.0);

        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(receiver);
        when(walletRepository.findByUser(receiver)).thenReturn(receiverWallet);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Transfer amount must be positive", body.get("message"));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void transferFundsInsufficientBalance() {
        transferRequest.setAmount(1000.0); // More than sender's balance

        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(receiver);
        when(walletRepository.findByUser(receiver)).thenReturn(receiverWallet);

        ResponseEntity<Map<String, Object>> response = walletService.transferFunds(sender, transferRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Insufficient balance", body.get("message"));

        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void getTransactionHistorySuccess() {
        TopUp topUp = new TopUp(100.0, senderWallet, TransactionType.TOPUP, "Credit Card (xxxx-xxxx-xxxx-1234)");
        Transfer transfer = new Transfer(50.0, TransactionType.TRANSFER, senderWallet, receiverWallet, "sender", "Test transfer");
        senderWallet.setTransactions(List.of(topUp, transfer));

        when(walletRepository.findByUser(sender)).thenReturn(senderWallet);

        ResponseEntity<Map<String, Object>> response = walletService.getTransactionHistory(sender);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Transaction history retrieved successfully", body.get("message"));

        List<Map<String, Object>> transactions = (List<Map<String, Object>>) body.get("transactions");
        assertEquals(2, transactions.size());

        Map<String, Object> topUpData = transactions.get(0);
        assertEquals(100.0, topUpData.get("amount"));
        assertEquals("TOPUP", topUpData.get("type"));
        assertEquals("Credit Card (xxxx-xxxx-xxxx-1234)", topUpData.get("provider"));

        Map<String, Object> transferData = transactions.get(1);
        assertEquals(50.0, transferData.get("amount"));
        assertEquals("TRANSFER", transferData.get("type"));
        assertEquals("Test transfer", transferData.get("note"));

        verify(walletRepository).findByUser(sender);
    }

    @Test
    void getTransactionHistoryWalletNotFound() {
        when(walletRepository.findByUser(sender)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = walletService.getTransactionHistory(sender);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Wallet not found", body.get("message"));

        verify(walletRepository).findByUser(sender);
    }
}