package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferRequestTest {

    @Test
    void testDefaultConstructor() {
        TransferRequest request = new TransferRequest();

        assertNull(request.getReceiverEmail());
        assertNull(request.getAmount());
        assertNull(request.getNote());
    }

    @Test
    void testAllArgsConstructor() {
        String receiverEmail = "receiver@example.com";
        Double amount = 100.0;
        String note = "Test transfer";

        TransferRequest request = new TransferRequest(receiverEmail, amount, note);

        assertEquals(receiverEmail, request.getReceiverEmail());
        assertEquals(amount, request.getAmount());
        assertEquals(note, request.getNote());
    }

    @Test
    void testSettersAndGetters() {
        TransferRequest request = new TransferRequest();

        String receiverEmail = "receiver@example.com";
        Double amount = 100.0;
        String note = "Test transfer";

        request.setReceiverEmail(receiverEmail);
        request.setAmount(amount);
        request.setNote(note);

        assertEquals(receiverEmail, request.getReceiverEmail());
        assertEquals(amount, request.getAmount());
        assertEquals(note, request.getNote());
    }

    @Test
    void testEqualsAndHashCode() {
        TransferRequest request1 = new TransferRequest(
                "receiver@example.com", 100.0, "Test transfer");
        TransferRequest request2 = new TransferRequest(
                "receiver@example.com", 100.0, "Test transfer");
        TransferRequest request3 = new TransferRequest(
                "other@example.com", 200.0, "Another transfer");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        TransferRequest request = new TransferRequest(
                "receiver@example.com", 100.0, "Test transfer");

        String toString = request.toString();

        assertTrue(toString.contains("receiverEmail=receiver@example.com"));
        assertTrue(toString.contains("amount=100.0"));
        assertTrue(toString.contains("note=Test transfer"));
    }
}