package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestTest {
    @Test
    void testDefaultConstructor() {
        PaymentRequest request = new PaymentRequest(1L, 10000.50, "Tes payment ini mah", "MERCH-1", "INV-001");

        assertEquals(1L, request.getWalletId());
        assertEquals(10000.50, request.getAmount());
        assertEquals("Tes payment ini mah", request.getDescription());
        assertEquals("MERCH-1", request.getMerchantId());
        assertEquals("INV-001", request.getInvoiceNumber());
    }
}