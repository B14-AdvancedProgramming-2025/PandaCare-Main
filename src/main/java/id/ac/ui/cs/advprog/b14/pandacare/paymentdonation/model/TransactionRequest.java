package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class TransactionRequest {
    private Long walletId;
    private Double amount;
    private String description;

    public TransactionRequest(Long walletId, Double amount, String description) {
        this.walletId = walletId;
        this.amount = amount;
        this.description = description;
    }
}