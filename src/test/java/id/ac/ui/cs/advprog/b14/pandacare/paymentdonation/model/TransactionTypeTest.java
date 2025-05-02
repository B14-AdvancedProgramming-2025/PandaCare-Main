package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {
    @Test
    void testEnumValues() {
        assertEquals(3, TransactionType.values().length);
        assertEquals(TransactionType.TOPUP, TransactionType.valueOf("TOPUP"));
        assertEquals(TransactionType.PAYMENT, TransactionType.valueOf("PAYMENT"));
        assertEquals(TransactionType.TRANSFER, TransactionType.valueOf("TRANSFER"));
    }
}