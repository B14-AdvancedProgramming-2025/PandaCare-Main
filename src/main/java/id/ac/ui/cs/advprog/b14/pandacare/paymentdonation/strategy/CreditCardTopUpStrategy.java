package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditCardTopUpStrategy implements TopUpStrategy {
    @Override
    public boolean processTopUp(Wallet wallet, TopUpRequest request) {
        if (!isValidCreditCard(request.getPaymentGatewayDetails())) {
            return false;
        }

        wallet.addBalance(request.getAmount());
        return true;
    }

    private boolean isValidCreditCard(Map<String, String> details) {
        return details.containsKey("cardNumber") &&
                details.containsKey("cvv") &&
                details.containsKey("expiry");
    }

    @Override
    public String getProviderName() {
        return "CREDIT_CARD";
    }
}