package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class TopUpRequest extends TransactionRequest {
    private Map<String, String> paymentGatewayDetails;

    public TopUpRequest(Long walletId, Double amount, String description, Map<String, String> paymentGatewayDetails) {
        super(walletId, amount, description);
        this.paymentGatewayDetails = paymentGatewayDetails;
    }
}