package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopUpRequestTest {

    @Test
    void testDefaultConstructor() {
        TopUpRequest request = new TopUpRequest();

        assertNull(request.getAmount());
        assertNull(request.getMethod());
        assertNull(request.getBankName());
        assertNull(request.getAccountNumber());
        assertNull(request.getCardNumber());
        assertNull(request.getCvv());
        assertNull(request.getExpiryDate());
        assertNull(request.getCardholderName());
    }

    @Test
    void testAllArgsConstructor() {
        Double amount = 100.0;
        String method = "CREDIT_CARD";
        String bankName = "Test Bank";
        String accountNumber = "123456789";
        String cardNumber = "1234567890123456";
        String cvv = "123";
        String expiryDate = "12/25";
        String cardholderName = "Test User";

        TopUpRequest request = new TopUpRequest(
                amount, method, bankName, accountNumber,
                cardNumber, cvv, expiryDate, cardholderName
        );

        assertEquals(amount, request.getAmount());
        assertEquals(method, request.getMethod());
        assertEquals(bankName, request.getBankName());
        assertEquals(accountNumber, request.getAccountNumber());
        assertEquals(cardNumber, request.getCardNumber());
        assertEquals(cvv, request.getCvv());
        assertEquals(expiryDate, request.getExpiryDate());
        assertEquals(cardholderName, request.getCardholderName());
    }

    @Test
    void testSettersAndGetters() {
        TopUpRequest request = new TopUpRequest();

        Double amount = 150.0;
        String method = "BANK_TRANSFER";
        String bankName = "Another Bank";
        String accountNumber = "987654321";
        String cardNumber = "9876543210987654";
        String cvv = "456";
        String expiryDate = "10/26";
        String cardholderName = "Another User";

        request.setAmount(amount);
        request.setMethod(method);
        request.setBankName(bankName);
        request.setAccountNumber(accountNumber);
        request.setCardNumber(cardNumber);
        request.setCvv(cvv);
        request.setExpiryDate(expiryDate);
        request.setCardholderName(cardholderName);

        assertEquals(amount, request.getAmount());
        assertEquals(method, request.getMethod());
        assertEquals(bankName, request.getBankName());
        assertEquals(accountNumber, request.getAccountNumber());
        assertEquals(cardNumber, request.getCardNumber());
        assertEquals(cvv, request.getCvv());
        assertEquals(expiryDate, request.getExpiryDate());
        assertEquals(cardholderName, request.getCardholderName());
    }

    @Test
    void testEqualsAndHashCode() {
        TopUpRequest request1 = new TopUpRequest(
                100.0, "CREDIT_CARD", null, null,
                "1234567890123456", "123", "12/25", "Test User"
        );

        TopUpRequest request2 = new TopUpRequest(
                100.0, "CREDIT_CARD", null, null,
                "1234567890123456", "123", "12/25", "Test User"
        );

        TopUpRequest request3 = new TopUpRequest(
                200.0, "BANK_TRANSFER", "Test Bank", "123456789",
                null, null, null, null
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        TopUpRequest request = new TopUpRequest(
                100.0, "CREDIT_CARD", null, null,
                "1234567890123456", "123", "12/25", "Test User"
        );

        String toString = request.toString();

        assertTrue(toString.contains("amount=100.0"));
        assertTrue(toString.contains("method=CREDIT_CARD"));
        assertTrue(toString.contains("cardNumber=1234567890123456"));
        assertTrue(toString.contains("cvv=123"));
        assertTrue(toString.contains("expiryDate=12/25"));
        assertTrue(toString.contains("cardholderName=Test User"));
    }
}