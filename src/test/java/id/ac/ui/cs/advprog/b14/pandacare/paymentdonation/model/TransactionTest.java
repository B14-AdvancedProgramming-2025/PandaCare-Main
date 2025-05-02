package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Mock
    private Wallet mockWallet;

    @Test
    void testDefaultConstructor() {
        MockitoAnnotations.openMocks(this);

        Transaction transaction = new Transaction();

        assertNull(transaction.getId());
        assertNull(transaction.getWallet());
        assertNull(transaction.getAmount());
        assertNull(transaction.getType());
        assertNull(transaction.getDescription());
        assertNull(transaction.getProvider());
        assertNotNull(transaction.getTimestamp());
        assertTrue(LocalDateTime.now().isAfter(transaction.getTimestamp()) ||
                LocalDateTime.now().isEqual(transaction.getTimestamp()));
    }

    @Test
    void testSetters() {
        MockitoAnnotations.openMocks(this);

        Transaction transaction = new Transaction();
        LocalDateTime testTime = LocalDateTime.now().minusHours(1);

        transaction.setId(1L);
        transaction.setWallet(mockWallet);
        transaction.setAmount(100.0);
        transaction.setType(TransactionType.TOPUP);
        transaction.setDescription("Test transaction");
        transaction.setProvider("Test Provider");
        transaction.setTimestamp(testTime);

        assertEquals(1L, transaction.getId());
        assertEquals(mockWallet, transaction.getWallet());
        assertEquals(100.0, transaction.getAmount());
        assertEquals(TransactionType.TOPUP, transaction.getType());
        assertEquals("Test transaction", transaction.getDescription());
        assertEquals("Test Provider", transaction.getProvider());
        assertEquals(testTime, transaction.getTimestamp());
    }
}