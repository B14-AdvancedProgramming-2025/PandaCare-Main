package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transfer;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;

public class TransferStrategy implements WalletStrategy {
    private final Wallet receiverWallet;
    private final String note;

    public TransferStrategy(Wallet receiverWallet, String note) {
        this.receiverWallet = receiverWallet;
        this.note = note;
    }

    @Override
    public Transfer execute(Wallet senderWallet, Double amount) {
        if (senderWallet.deductBalance(amount)) {
            receiverWallet.addBalance(amount);

            Transfer senderTransfer = new Transfer(
                    amount,
                    TransactionType.TRANSFER,
                    senderWallet,
                    receiverWallet,
                    "sender",
                    note
            );
            senderWallet.getTransactions().add(senderTransfer);

            Transfer receiverTransfer = new Transfer(
                    amount,
                    TransactionType.TRANSFER,
                    senderWallet,
                    receiverWallet,
                    "receiver",
                    note
            );
            receiverWallet.getTransactions().add(receiverTransfer);

            return senderTransfer;
        }
        return null;
    }
}