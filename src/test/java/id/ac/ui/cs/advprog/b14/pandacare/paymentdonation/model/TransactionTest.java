package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TransactionTest {

    // Concrete implementation of the abstract Transaction class for testing
    private static class ConcreteTransaction extends Transaction {
        public ConcreteTransaction() {
            super();
        }

        public ConcreteTransaction(Double amount, String description, Wallet wallet, TransactionType type) {
            super(amount, description, wallet, type);
        }
    }

    @Test
    void testDefaultConstructor() {
        ConcreteTransaction transaction = new ConcreteTransaction();

        assertNull(transaction.getId());
        assertNull(transaction.getAmount());
        assertNull(transaction.getDescription());
        assertNull(transaction.getType());
        assertNotNull(transaction.getTimestamp());
        assertNull(transaction.getWallet());
    }

    @Test
    void testParameterizedConstructor() {
        Double amount = 100.0;
        String description = "Test transaction";
        Wallet mockWallet = mock(Wallet.class);
        TransactionType type = TransactionType.TOPUP;

        ConcreteTransaction transaction = new ConcreteTransaction(amount, description, mockWallet, type);

        assertNull(transaction.getId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(mockWallet, transaction.getWallet());
        assertEquals(type, transaction.getType());
        assertNotNull(transaction.getTimestamp());
    }

    @Test
    void testSetters() {
        ConcreteTransaction transaction = new ConcreteTransaction();
        Long id = 1L;
        Double amount = 100.0;
        String description = "Test transaction";
        Wallet mockWallet = mock(Wallet.class);
        TransactionType type = TransactionType.TOPUP;
        LocalDateTime timestamp = LocalDateTime.now();

        transaction.setId(id);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setWallet(mockWallet);
        transaction.setType(type);
        transaction.setTimestamp(timestamp);

        assertEquals(id, transaction.getId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(mockWallet, transaction.getWallet());
        assertEquals(type, transaction.getType());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testTransactionTypeEnum() {
        assertEquals("TOPUP", TransactionType.TOPUP.name());
        assertEquals("TIP", TransactionType.TIP.name());
        assertEquals("TRANSFER", TransactionType.TRANSFER.name());
    }
}