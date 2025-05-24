package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WalletTest {

    @Test
    void testDefaultConstructor() {
        Wallet wallet = new Wallet();

        assertNull(wallet.getId());
        assertEquals(0.0, wallet.getBalance());
        assertNull(wallet.getUser());
        assertNotNull(wallet.getTransactions());
        assertTrue(wallet.getTransactions().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        User mockUser = mock(User.class);
        Wallet wallet = new Wallet(mockUser);

        assertNull(wallet.getId());
        assertEquals(0.0, wallet.getBalance());
        assertEquals(mockUser, wallet.getUser());
        assertNotNull(wallet.getTransactions());
        assertTrue(wallet.getTransactions().isEmpty());
    }

    @Test
    void testSetters() {
        Wallet wallet = new Wallet();
        User mockUser = mock(User.class);
        Long id = 1L;
        Double balance = 100.0;
        List<Transaction> transactions = new ArrayList<>();

        wallet.setId(id);
        wallet.setBalance(balance);
        wallet.setUser(mockUser);
        wallet.setTransactions(transactions);

        assertEquals(id, wallet.getId());
        assertEquals(balance, wallet.getBalance());
        assertEquals(mockUser, wallet.getUser());
        assertEquals(transactions, wallet.getTransactions());
    }

    @Test
    void testDeductBalanceSuccessful() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);

        boolean result = wallet.deductBalance(50.0);

        assertTrue(result);
        assertEquals(50.0, wallet.getBalance());
    }

    @Test
    void testDeductBalanceInsufficientFunds() {
        Wallet wallet = new Wallet();
        wallet.setBalance(30.0);

        boolean result = wallet.deductBalance(50.0);

        assertFalse(result);
        assertEquals(30.0, wallet.getBalance());
    }

    @Test
    void testDeductBalanceZeroAmount() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);

        boolean result = wallet.deductBalance(0.0);

        assertFalse(result);
        assertEquals(100.0, wallet.getBalance());
    }

    @Test
    void testDeductBalanceNegativeAmount() {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);

        boolean result = wallet.deductBalance(-10.0);

        assertFalse(result);
        assertEquals(100.0, wallet.getBalance());
    }

    @Test
    void testAddBalanceSuccessful() {
        Wallet wallet = new Wallet();
        wallet.setBalance(50.0);

        boolean result = wallet.addBalance(30.0);

        assertTrue(result);
        assertEquals(80.0, wallet.getBalance());
    }

    @Test
    void testAddBalanceZeroAmount() {
        Wallet wallet = new Wallet();
        wallet.setBalance(50.0);

        boolean result = wallet.addBalance(0.0);

        assertFalse(result);
        assertEquals(50.0, wallet.getBalance());
    }

    @Test
    void testAddBalanceNegativeAmount() {
        Wallet wallet = new Wallet();
        wallet.setBalance(50.0);

        boolean result = wallet.addBalance(-10.0);

        assertFalse(result);
        assertEquals(50.0, wallet.getBalance());
    }
}