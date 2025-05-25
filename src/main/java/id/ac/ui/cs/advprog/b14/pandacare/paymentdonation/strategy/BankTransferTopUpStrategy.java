package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;

public class BankTransferTopUpStrategy implements TopUpStrategy {
    private final String bankName;
    private final String accountNumber;

    public BankTransferTopUpStrategy(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public TopUp execute(Wallet wallet, Double amount) {
        if (wallet.addBalance(amount)) {
            String provider = String.format("Bank Transfer (%s: %s)", bankName, accountNumber);
            TopUp topUp = new TopUp(amount, wallet, TransactionType.TOPUP, provider);
            wallet.getTransactions().add(topUp);
            return topUp;
        }
        return null;
    }
}