package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transaction;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transfer;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WalletStrategyTest {
    private Wallet senderWallet;
    private Wallet receiverWallet;
    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = new Pacilian(
                "pacil-123",
                "sender@example.com",
                "password123",
                "Sender User",
                "1234567890123456",
                "123 Sender Street",
                "08123456789",
                Arrays.asList("None")
        );
        receiver = new Pacilian(
                "pacil-456",
                "receiver@example.com",
                "password456",
                "Receiver User",
                "6543210987654321",
                "456 Receiver Street",
                "08987654321",
                Arrays.asList("None")
        );
        senderWallet = new Wallet(sender);
        receiverWallet = new Wallet(receiver);
        senderWallet.setBalance(200.0);
        receiverWallet.setBalance(50.0);
        senderWallet.setTransactions(new ArrayList<>());
        receiverWallet.setTransactions(new ArrayList<>());
    }

    @Test
    void testTransferStrategySuccess() {
        String note = "Test transfer";
        TransferStrategy strategy = new TransferStrategy(receiverWallet, note);

        Double amount = 100.0;
        Transfer transfer = strategy.execute(senderWallet, amount);

        assertNotNull(transfer);

        assertEquals(100.0, senderWallet.getBalance());
        assertEquals(150.0, receiverWallet.getBalance());

        assertEquals(1, senderWallet.getTransactions().size());
        assertEquals(1, receiverWallet.getTransactions().size());

        Transaction senderTransaction = senderWallet.getTransactions().getFirst();
        assertInstanceOf(Transfer.class, senderTransaction);
        assertEquals(amount, senderTransaction.getAmount());
        assertEquals(TransactionType.TRANSFER, senderTransaction.getType());
        assertEquals(senderWallet, senderTransaction.getWallet());
        assertTrue(senderTransaction.getDescription().contains("Transfer"));
        assertTrue(senderTransaction.getDescription().contains(sender.getName()));
        assertTrue(senderTransaction.getDescription().contains(receiver.getName()));

        Transaction receiverTransaction = receiverWallet.getTransactions().getFirst();
        assertInstanceOf(Transfer.class, receiverTransaction);
        assertEquals(amount, receiverTransaction.getAmount());
        assertEquals(TransactionType.TRANSFER, receiverTransaction.getType());
        assertEquals(receiverWallet, receiverTransaction.getWallet());
        assertTrue(receiverTransaction.getDescription().contains("Transfer"));
        assertTrue(receiverTransaction.getDescription().contains(sender.getName()));
        assertTrue(receiverTransaction.getDescription().contains(receiver.getName()));

        assertEquals(note, ((Transfer) senderTransaction).getNote());
        assertEquals(note, ((Transfer) receiverTransaction).getNote());

        assertEquals(senderWallet, ((Transfer) senderTransaction).getSenderWallet());
        assertEquals(receiverWallet, ((Transfer) senderTransaction).getReceiverWallet());
        assertEquals(senderWallet, ((Transfer) receiverTransaction).getSenderWallet());
        assertEquals(receiverWallet, ((Transfer) receiverTransaction).getReceiverWallet());
    }

    @Test
    void testTransferStrategyInsufficientFunds() {
        TransferStrategy strategy = new TransferStrategy(receiverWallet, "Test transfer");

        Double amount = 300.0;
        Transfer transfer = strategy.execute(senderWallet, amount);

        assertNull(transfer);

        assertEquals(200.0, senderWallet.getBalance());
        assertEquals(50.0, receiverWallet.getBalance());

        assertEquals(0, senderWallet.getTransactions().size());
        assertEquals(0, receiverWallet.getTransactions().size());
    }

    @Test
    void testTransferStrategyZeroAmount() {
        TransferStrategy strategy = new TransferStrategy(receiverWallet, "Test transfer");

        Double amount = 0.0;
        Transfer transfer = strategy.execute(senderWallet, amount);

        assertNull(transfer);

        assertEquals(200.0, senderWallet.getBalance());
        assertEquals(50.0, receiverWallet.getBalance());

        assertEquals(0, senderWallet.getTransactions().size());
        assertEquals(0, receiverWallet.getTransactions().size());
    }

    @Test
    void testTransferStrategyNegativeAmount() {
        TransferStrategy strategy = new TransferStrategy(receiverWallet, "Test transfer");

        Double amount = -50.0;
        Transfer transfer = strategy.execute(senderWallet, amount);

        assertNull(transfer);

        assertEquals(200.0, senderWallet.getBalance());
        assertEquals(50.0, receiverWallet.getBalance());

        assertEquals(0, senderWallet.getTransactions().size());
        assertEquals(0, receiverWallet.getTransactions().size());
    }

    @Test
    void testMultipleTransfers() {
        TransferStrategy strategy = new TransferStrategy(receiverWallet, "First transfer");

        strategy.execute(senderWallet, 50.0);

        TransferStrategy secondStrategy = new TransferStrategy(receiverWallet, "Second transfer");
        secondStrategy.execute(senderWallet, 30.0);

        assertEquals(120.0, senderWallet.getBalance());
        assertEquals(130.0, receiverWallet.getBalance());

        assertEquals(2, senderWallet.getTransactions().size());
        assertEquals(2, receiverWallet.getTransactions().size());
    }
}