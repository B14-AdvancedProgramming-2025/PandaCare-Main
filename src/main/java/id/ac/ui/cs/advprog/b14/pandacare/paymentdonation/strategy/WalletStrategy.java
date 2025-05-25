package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transaction;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;

public interface WalletStrategy {
    Transaction execute(Wallet wallet, Double amount);
}