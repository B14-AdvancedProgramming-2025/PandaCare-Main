package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;

public class CreditCardTopUpStrategy implements TopUpStrategy {
    private final String cardNumber;
    private final String cvv;
    private final String expiryDate;
    private final String cardholderName;

    public CreditCardTopUpStrategy(String cardNumber, String cvv, String expiryDate, String cardholderName) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardholderName = cardholderName;
    }

    @Override
    public TopUp execute(Wallet wallet, Double amount) {
        if (wallet.addBalance(amount)) {
            // Mask card number for security
            String maskedCardNumber = "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);
            String provider = String.format("Credit Card (%s)", maskedCardNumber);

            TopUp topUp = new TopUp(amount, wallet, TransactionType.TOPUP, provider);
            wallet.getTransactions().add(topUp);
            return topUp;
        }
        return null;
    }
}