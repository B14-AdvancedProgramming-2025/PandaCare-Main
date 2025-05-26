package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TopUpTest {

    @Test
    void testDefaultConstructor() {
        TopUp topUp = new TopUp();

        assertNull(topUp.getId());
        assertNull(topUp.getAmount());
        assertNull(topUp.getDescription());
        assertNull(topUp.getWallet());
        assertNull(topUp.getType());
        assertNotNull(topUp.getTimestamp());
        assertNull(topUp.getProvider());
    }

    @Test
    void testParameterizedConstructor() {
        Double amount = 100.0;
        Wallet mockWallet = mock(Wallet.class);
        TransactionType type = TransactionType.TOPUP;
        String provider = "Credit Card (xxxx-xxxx-xxxx-1234)";

        TopUp topUp = new TopUp(amount, mockWallet, type, provider);

        assertNull(topUp.getId());
        assertEquals(amount, topUp.getAmount());
        assertEquals(mockWallet, topUp.getWallet());
        assertEquals(type, topUp.getType());
        assertEquals(provider, topUp.getProvider());
        assertNotNull(topUp.getTimestamp());
        assertEquals("TopUp 100.00 from Credit Card (xxxx-xxxx-xxxx-1234)", topUp.getDescription());
    }

    @Test
    void testSetProvider() {
        TopUp topUp = new TopUp();
        String provider = "Bank Transfer (BCA: 1234567890)";

        topUp.setProvider(provider);

        assertEquals(provider, topUp.getProvider());
    }
}