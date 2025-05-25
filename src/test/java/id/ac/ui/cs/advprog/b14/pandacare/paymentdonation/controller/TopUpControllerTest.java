package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.TopUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class TopUpControllerTest {

    @Mock
    private TopUpService topUpService;

    @InjectMocks
    private TopUpController topUpController;

    private MockMvc mockMvc;
    private User user;
    private TopUpRequest bankTransferRequest;
    private TopUpRequest creditCardRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(topUpController).build();

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
    void processTopUpDelegatesCorrectlyToService() {
        // Setup mock response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Topped-Up successfully");

        Map<String, Object> data = new HashMap<>();
        data.put("amount", 50.0);
        data.put("balance", 150.0);
        data.put("provider", "Bank Transfer (Test Bank: 123456789)");
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(topUpService.processTopUp(user, bankTransferRequest)).thenReturn(expectedResponse);

        // Execute and verify
        ResponseEntity<Map<String, Object>> response =
                topUpController.processTopUp(user, bankTransferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
        verify(topUpService).processTopUp(user, bankTransferRequest);
    }

    @Test
    void processTopUpWithBankTransferReturnsCorrectResponse() {
        // Setup mock response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Topped-Up successfully");

        Map<String, Object> data = new HashMap<>();
        data.put("amount", 50.0);
        data.put("balance", 150.0);
        data.put("provider", "Bank Transfer (Test Bank: 123456789)");
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(topUpService.processTopUp(any(User.class), any(TopUpRequest.class)))
                .thenReturn(expectedResponse);

        // Execute and verify
        ResponseEntity<Map<String, Object>> response =
                topUpController.processTopUp(user, bankTransferRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));
        assertEquals("Topped-Up successfully", body.get("message"));

        Map<String, Object> responseData = (Map<String, Object>) body.get("data");
        assertEquals(50.0, responseData.get("amount"));
        assertEquals(150.0, responseData.get("balance"));
        assertEquals("Bank Transfer (Test Bank: 123456789)", responseData.get("provider"));
    }

    @Test
    void processTopUpWithCreditCardReturnsCorrectResponse() {
        // Setup mock response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Topped-Up successfully");

        Map<String, Object> data = new HashMap<>();
        data.put("amount", 100.0);
        data.put("balance", 200.0);
        data.put("provider", "Credit Card (xxxx-xxxx-xxxx-1111)");
        responseBody.put("data", data);

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.OK).body(responseBody);

        when(topUpService.processTopUp(any(User.class), any(TopUpRequest.class)))
                .thenReturn(expectedResponse);

        // Execute and verify
        ResponseEntity<Map<String, Object>> response =
                topUpController.processTopUp(user, creditCardRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue((Boolean) body.get("success"));

        Map<String, Object> responseData = (Map<String, Object>) body.get("data");
        assertEquals(100.0, responseData.get("amount"));
        assertEquals(200.0, responseData.get("balance"));
        assertTrue(((String) responseData.get("provider")).contains("Credit Card"));
    }

    @Test
    void processTopUpWithErrorReturnsErrorResponse() {
        // Setup mock error response
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "Invalid card number");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        when(topUpService.processTopUp(any(User.class), any(TopUpRequest.class)))
                .thenReturn(expectedResponse);

        // Execute and verify
        ResponseEntity<Map<String, Object>> response =
                topUpController.processTopUp(user, creditCardRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Invalid card number", body.get("message"));
    }

    @Test
    void processTopUpWithWalletNotFoundReturnsNotFoundResponse() {
        // Setup mock not found response
        Map<String, Object> notFoundResponse = new HashMap<>();
        notFoundResponse.put("success", false);
        notFoundResponse.put("message", "Wallet not found");

        ResponseEntity<Map<String, Object>> expectedResponse =
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);

        when(topUpService.processTopUp(any(User.class), any(TopUpRequest.class)))
                .thenReturn(expectedResponse);

        // Execute and verify
        ResponseEntity<Map<String, Object>> response =
                topUpController.processTopUp(user, bankTransferRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertFalse((Boolean) body.get("success"));
        assertEquals("Wallet not found", body.get("message"));
    }
}