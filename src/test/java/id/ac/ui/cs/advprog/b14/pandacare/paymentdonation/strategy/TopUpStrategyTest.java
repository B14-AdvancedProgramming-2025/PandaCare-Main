package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TopUpStrategyTest {
    private Wallet wallet;
    private User user;

    @BeforeEach
    void setUp() {
        user = new Pacilian(
                "pacil-123",
                "test@example.com",
                "password123",
                "Test User",
                "1234567890123456",
                "123 Test Street",
                "08123456789",
                Arrays.asList("None")
        );
        wallet = new Wallet(user);
        wallet.setBalance(0.0);
        wallet.setTransactions(new ArrayList<>());
    }

    @Test
    void testCreditCardTopUpStrategySuccess() {
        CreditCardTopUpStrategy strategy = new CreditCardTopUpStrategy(
                "4111111111111111", "123", "12/25", "Test User"
        );

        Double amount = 100.0;
        TopUp topUp = strategy.execute(wallet, amount);

        assertNotNull(topUp);
        assertEquals(amount, wallet.getBalance());
        assertEquals(1, wallet.getTransactions().size());
        assertEquals(amount, topUp.getAmount());
        assertEquals(wallet, topUp.getWallet());
        assertEquals(TransactionType.TOPUP, topUp.getType());
        assertTrue(topUp.getProvider().contains("Credit Card"));
        assertTrue(topUp.getProvider().contains("xxxx-xxxx-xxxx-1111"));
    }

    @Test
    void testCreditCardTopUpStrategyZeroAmount() {
        CreditCardTopUpStrategy strategy = new CreditCardTopUpStrategy(
                "4111111111111111", "123", "12/25", "Test User"
        );

        TopUp topUp = strategy.execute(wallet, 0.0);

        assertNull(topUp);
        assertEquals(0.0, wallet.getBalance());
        assertEquals(0, wallet.getTransactions().size());
    }

    @Test
    void testCreditCardTopUpStrategyNegativeAmount() {
        CreditCardTopUpStrategy strategy = new CreditCardTopUpStrategy(
                "4111111111111111", "123", "12/25", "Test User"
        );

        TopUp topUp = strategy.execute(wallet, -50.0);

        assertNull(topUp);
        assertEquals(0.0, wallet.getBalance());
        assertEquals(0, wallet.getTransactions().size());
    }

    @Test
    void testBankTransferTopUpStrategySuccess() {
        BankTransferTopUpStrategy strategy = new BankTransferTopUpStrategy(
                "Test Bank", "12345678"
        );

        Double amount = 150.0;
        TopUp topUp = strategy.execute(wallet, amount);

        assertNotNull(topUp);
        assertEquals(amount, wallet.getBalance());
        assertEquals(1, wallet.getTransactions().size());
        assertEquals(amount, topUp.getAmount());
        assertEquals(wallet, topUp.getWallet());
        assertEquals(TransactionType.TOPUP, topUp.getType());
        assertTrue(topUp.getProvider().contains("Bank Transfer"));
        assertTrue(topUp.getProvider().contains("Test Bank"));
        assertTrue(topUp.getProvider().contains("12345678"));
    }

    @Test
    void testBankTransferTopUpStrategyZeroAmount() {
        BankTransferTopUpStrategy strategy = new BankTransferTopUpStrategy(
                "Test Bank", "12345678"
        );

        TopUp topUp = strategy.execute(wallet, 0.0);

        assertNull(topUp);
        assertEquals(0.0, wallet.getBalance());
        assertEquals(0, wallet.getTransactions().size());
    }

    @Test
    void testBankTransferTopUpStrategyNegativeAmount() {
        BankTransferTopUpStrategy strategy = new BankTransferTopUpStrategy(
                "Test Bank", "12345678"
        );

        TopUp topUp = strategy.execute(wallet, -50.0);

        assertNull(topUp);
        assertEquals(0.0, wallet.getBalance());
        assertEquals(0, wallet.getTransactions().size());
    }

    @Test
    void testMultipleTopUps() {
        CreditCardTopUpStrategy creditCardStrategy = new CreditCardTopUpStrategy(
                "4111111111111111", "123", "12/25", "Test User"
        );
        BankTransferTopUpStrategy bankTransferStrategy = new BankTransferTopUpStrategy(
                "Test Bank", "12345678"
        );

        creditCardStrategy.execute(wallet, 100.0);
        bankTransferStrategy.execute(wallet, 150.0);

        assertEquals(250.0, wallet.getBalance());
        assertEquals(2, wallet.getTransactions().size());
    }
}