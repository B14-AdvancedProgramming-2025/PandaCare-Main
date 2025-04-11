package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.WalletStrategy;

public class WalletService {
    private final WalletStrategy walletStrategy;

    public WalletService(WalletStrategy walletStrategy) {
        this.walletStrategy = walletStrategy;
    }

    public Wallet transfer(TransactionRequest transactionRequest) {
        return walletStrategy.transfer(transactionRequest);
    }
}