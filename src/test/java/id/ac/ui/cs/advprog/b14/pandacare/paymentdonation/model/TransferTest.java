package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransferTest {

    @Test
    void testDefaultConstructor() {
        Transfer transfer = new Transfer();

        assertNull(transfer.getId());
        assertNull(transfer.getAmount());
        assertNull(transfer.getDescription());
        assertNull(transfer.getWallet());
        assertNull(transfer.getType());
        assertNotNull(transfer.getTimestamp());
        assertNull(transfer.getSenderWallet());
        assertNull(transfer.getReceiverWallet());
        assertNull(transfer.getNote());
    }

    @Test
    void testParameterizedConstructorWithTransferType() {
        Wallet mockSenderWallet = mock(Wallet.class);
        Wallet mockReceiverWallet = mock(Wallet.class);
        User mockSenderUser = mock(User.class);
        User mockReceiverUser = mock(User.class);

        when(mockSenderWallet.getUser()).thenReturn(mockSenderUser);
        when(mockReceiverWallet.getUser()).thenReturn(mockReceiverUser);
        when(mockSenderUser.getName()).thenReturn("Sender Name");
        when(mockReceiverUser.getName()).thenReturn("Receiver Name");

        Double amount = 100.0;
        String note = "Test transfer note";
        String userPosition = "sender";

        Transfer transfer = new Transfer(amount, TransactionType.TRANSFER,
                mockSenderWallet, mockReceiverWallet, userPosition, note);

        assertNull(transfer.getId());
        assertEquals(amount, transfer.getAmount());
        assertEquals(mockSenderWallet, transfer.getWallet());
        assertEquals(TransactionType.TRANSFER, transfer.getType());
        assertNotNull(transfer.getTimestamp());
        assertEquals(mockSenderWallet, transfer.getSenderWallet());
        assertEquals(mockReceiverWallet, transfer.getReceiverWallet());
        assertEquals(note, transfer.getNote());
        assertEquals("Transfer 100.00 from Sender Name to Receiver Name", transfer.getDescription());
    }

    @Test
    void testParameterizedConstructorWithTipType() {
        Wallet mockSenderWallet = mock(Wallet.class);
        Wallet mockReceiverWallet = mock(Wallet.class);
        User mockSenderUser = mock(User.class);
        User mockReceiverUser = mock(User.class);

        when(mockSenderWallet.getUser()).thenReturn(mockSenderUser);
        when(mockReceiverWallet.getUser()).thenReturn(mockReceiverUser);
        when(mockSenderUser.getName()).thenReturn("Sender Name");
        when(mockReceiverUser.getName()).thenReturn("Receiver Name");

        Double amount = 50.0;
        String note = "Test tip note";
        String userPosition = "receiver";

        Transfer transfer = new Transfer(amount, TransactionType.TIP,
                mockSenderWallet, mockReceiverWallet, userPosition, note);

        assertNull(transfer.getId());
        assertEquals(amount, transfer.getAmount());
        assertEquals(mockReceiverWallet, transfer.getWallet()); // Based on userPosition being "receiver"
        assertEquals(TransactionType.TIP, transfer.getType());
        assertNotNull(transfer.getTimestamp());
        assertEquals(mockSenderWallet, transfer.getSenderWallet());
        assertEquals(mockReceiverWallet, transfer.getReceiverWallet());
        assertEquals(note, transfer.getNote());
        assertEquals("Tip 50.00 from Sender Name to Receiver Name", transfer.getDescription());
    }

    @Test
    void testSetters() {
        Transfer transfer = new Transfer();
        Long id = 1L;
        Wallet mockSenderWallet = mock(Wallet.class);
        Wallet mockReceiverWallet = mock(Wallet.class);
        String note = "Updated note";

        transfer.setId(id);
        transfer.setSenderWallet(mockSenderWallet);
        transfer.setReceiverWallet(mockReceiverWallet);
        transfer.setNote(note);

        assertEquals(id, transfer.getId());
        assertEquals(mockSenderWallet, transfer.getSenderWallet());
        assertEquals(mockReceiverWallet, transfer.getReceiverWallet());
        assertEquals(note, transfer.getNote());
    }
}