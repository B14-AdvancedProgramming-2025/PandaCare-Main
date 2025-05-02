package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionRequestTest {
    @Test
    void testDefaultConstructor() {
        TransactionRequest request = new TransactionRequest(1L, 10000.50, "Ini hanyalah tes semata") {};

        assertEquals(1, request.getWalletId());
        assertEquals(10000.50, request.getAmount());
        assertEquals("Ini hanyalah tes semata", request.getDescription());
    }
}