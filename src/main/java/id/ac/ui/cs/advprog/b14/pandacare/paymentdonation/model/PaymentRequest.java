package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentRequest extends TransactionRequest {
    private String merchantId;
    private String invoiceNumber;

    public PaymentRequest(Long walletId, Double amount, String description, String merchantId, String invoiceNumber) {
        super(walletId, amount, description);
        this.merchantId = merchantId;
        this.invoiceNumber = invoiceNumber;
    }
}