package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankTransferTopUpStrategy implements TopUpStrategy {
    @Override
    public boolean processTopUp(Wallet wallet, TopUpRequest request) {
        if (!isValidBankTransfer(request.getPaymentGatewayDetails())) {
            return false;
        }

        wallet.addBalance(request.getAmount());
        return true;
    }

    private boolean isValidBankTransfer(Map<String, String> details) {
        return details.containsKey("accountNumber") && details.containsKey("bankName");
    }

    @Override
    public String getProviderName() {
        return "BANK_TRANSFER";
    }
}