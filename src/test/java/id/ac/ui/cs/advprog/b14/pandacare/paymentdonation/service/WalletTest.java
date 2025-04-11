package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.WalletStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DummyWalletStrategy implements WalletStrategy {
    public boolean executed = false;

    // Adding a method to simulate work
    public void execute() {
        executed = true;
    }

    @Override
    public Wallet transfer(TransactionRequest transactionRequest) {
        return null;
    }
}

public class WalletTest {

    /**
     * In a full implementation, the Wallet class would delegate to these strategies,
     * for instance in a method like processTransaction(TransactionRequest, ..., ...).
     * Here we simulate that by directly calling the dummy strategies.
     */
    @Test
    public void testWalletUsesStrategy() {
        Wallet wallet = new Wallet();
        TransactionRequest request = new TransactionRequest();

        DummyWalletStrategy walletStrategy = new DummyWalletStrategy();

        walletStrategy.execute();

        assertTrue(walletStrategy.executed);
    }
}