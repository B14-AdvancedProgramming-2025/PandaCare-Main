package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferRequest extends TransactionRequest {
    private Long recipientWalletId;
    private String transferReference;

    public TransferRequest(Long walletId, Double amount, String description, Long recipientWalletId, String transferReference) {
        super(walletId, amount, description);
        this.recipientWalletId = recipientWalletId;
        this.transferReference = transferReference;
    }
}