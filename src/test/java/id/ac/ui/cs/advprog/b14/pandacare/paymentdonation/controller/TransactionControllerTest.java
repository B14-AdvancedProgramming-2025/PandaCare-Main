package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TransferRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;
    private User user;
    private TransferRequest transferRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

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

        transferRequest = new TransferRequest();
        transferRequest.setReceiverEmail("receiver@example.com");
        transferRequest.setAmount(100.0);
        transferRequest.setNote("Test transfer");
    }

    @Test
    void getBalanceDelegatesCorrectlyToService() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Balance retrieved successfully");

        Map<String, Object> data = new HashMap<>();
        data.put("balance", 500.0);
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(walletService.getBalance(user)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = transactionController.getBalance(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
        verify(walletService).getBalance(user);
    }

    @Test
    void getBalanceReturnsCorrectResponse() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Balance retrieved successfully");

        Map<String, Object> data = new HashMap<>();
        data.put("balance", 500.0);
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(walletService.getBalance(any(User.class))).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = transactionController.getBalance(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Balance retrieved successfully", body.get("message"));

        Map<String, Object> responseData = (Map<String, Object>) body.get("data");
        assertEquals(500.0, responseData.get("balance"));
    }

    @Test
    void getBalanceWithWalletNotFoundReturnsNotFoundResponse() {
        Map<String, Object> notFoundResponse = new HashMap<>();
        notFoundResponse.put("success", false);
        notFoundResponse.put("message", "Wallet not found");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);

        when(walletService.getBalance(any(User.class))).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = transactionController.getBalance(user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Wallet not found", body.get("message"));
    }

    @Test
    void transferFundsDelegatesCorrectlyToService() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Transfer successful");

        Map<String, Object> data = new HashMap<>();
        data.put("sender", "Test User");
        data.put("senderBalance", 400.0);
        data.put("receiver", "Receiver Name");
        data.put("receiverBalance", 300.0);
        data.put("amount", 100.0);
        data.put("note", "Test transfer");
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(walletService.transferFunds(user, transferRequest)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.transferFunds(user, transferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
        verify(walletService).transferFunds(user, transferRequest);
    }

    @Test
    void transferFundsReturnsCorrectResponse() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Transfer successful");

        Map<String, Object> data = new HashMap<>();
        data.put("sender", "Test User");
        data.put("senderBalance", 400.0);
        data.put("receiver", "Receiver Name");
        data.put("receiverBalance", 300.0);
        data.put("amount", 100.0);
        data.put("note", "Test transfer");
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(walletService.transferFunds(any(User.class), any(TransferRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.transferFunds(user, transferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Transfer successful", body.get("message"));

        Map<String, Object> responseData = (Map<String, Object>) body.get("data");
        assertEquals("Test User", responseData.get("sender"));
        assertEquals(400.0, responseData.get("senderBalance"));
        assertEquals("Receiver Name", responseData.get("receiver"));
        assertEquals(300.0, responseData.get("receiverBalance"));
        assertEquals(100.0, responseData.get("amount"));
        assertEquals("Test transfer", responseData.get("note"));
    }

    @Test
    void transferFundsWithWalletNotFoundReturnsNotFoundResponse() {
        Map<String, Object> notFoundResponse = new HashMap<>();
        notFoundResponse.put("success", false);
        notFoundResponse.put("message", "Sender wallet not found");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);

        when(walletService.transferFunds(any(User.class), any(TransferRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.transferFunds(user, transferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Sender wallet not found", body.get("message"));
    }

    @Test
    void transferFundsWithReceiverNotFoundReturnsNotFoundResponse() {
        Map<String, Object> notFoundResponse = new HashMap<>();
        notFoundResponse.put("success", false);
        notFoundResponse.put("message", "Receiver not found");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);

        when(walletService.transferFunds(any(User.class), any(TransferRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.transferFunds(user, transferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Receiver not found", body.get("message"));
    }

    @Test
    void transferFundsWithInsufficientBalanceReturnsBadRequestResponse() {
        Map<String, Object> badRequestResponse = new HashMap<>();
        badRequestResponse.put("success", false);
        badRequestResponse.put("message", "Insufficient balance");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);

        when(walletService.transferFunds(any(User.class), any(TransferRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.transferFunds(user, transferRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Insufficient balance", body.get("message"));
    }

    @Test
    void getTransactionHistoryDelegatesCorrectlyToService() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Transaction history retrieved successfully");

        List<Map<String, Object>> transactions = new ArrayList<>();
        Map<String, Object> transaction1 = new HashMap<>();
        transaction1.put("amount", 100.0);
        transaction1.put("type", "TOPUP");
        transaction1.put("provider", "Credit Card (xxxx-xxxx-xxxx-1234)");

        Map<String, Object> transaction2 = new HashMap<>();
        transaction2.put("amount", 50.0);
        transaction2.put("type", "TRANSFER");
        transaction2.put("note", "Test transfer");

        transactions.add(transaction1);
        transactions.add(transaction2);
        responseBody.put("transactions", transactions);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(walletService.getTransactionHistory(user)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.getTransactionHistory(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
        verify(walletService).getTransactionHistory(user);
    }

    @Test
    void getTransactionHistoryReturnsCorrectResponse() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Transaction history retrieved successfully");

        List<Map<String, Object>> transactions = new ArrayList<>();
        Map<String, Object> transaction1 = new HashMap<>();
        transaction1.put("amount", 100.0);
        transaction1.put("type", "TOPUP");
        transaction1.put("provider", "Credit Card (xxxx-xxxx-xxxx-1234)");

        Map<String, Object> transaction2 = new HashMap<>();
        transaction2.put("amount", 50.0);
        transaction2.put("type", "TRANSFER");
        transaction2.put("note", "Test transfer");

        transactions.add(transaction1);
        transactions.add(transaction2);
        responseBody.put("transactions", transactions);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(walletService.getTransactionHistory(any(User.class))).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.getTransactionHistory(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Transaction history retrieved successfully", body.get("message"));

        List<Map<String, Object>> responseTransactions = (List<Map<String, Object>>) body.get("transactions");
        assertEquals(2, responseTransactions.size());

        Map<String, Object> topUpTransaction = responseTransactions.get(0);
        assertEquals(100.0, topUpTransaction.get("amount"));
        assertEquals("TOPUP", topUpTransaction.get("type"));
        assertEquals("Credit Card (xxxx-xxxx-xxxx-1234)", topUpTransaction.get("provider"));

        Map<String, Object> transferTransaction = responseTransactions.get(1);
        assertEquals(50.0, transferTransaction.get("amount"));
        assertEquals("TRANSFER", transferTransaction.get("type"));
        assertEquals("Test transfer", transferTransaction.get("note"));
    }

    @Test
    void getTransactionHistoryWithWalletNotFoundReturnsNotFoundResponse() {
        Map<String, Object> notFoundResponse = new HashMap<>();
        notFoundResponse.put("success", false);
        notFoundResponse.put("message", "Wallet not found");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);

        when(walletService.getTransactionHistory(any(User.class))).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response =
                transactionController.getTransactionHistory(user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Wallet not found", body.get("message"));
    }
}