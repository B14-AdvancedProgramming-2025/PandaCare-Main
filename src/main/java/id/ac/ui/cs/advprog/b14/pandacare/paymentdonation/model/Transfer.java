package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name= "transfers")
@Getter @Setter
public class Transfer extends Transaction {
    @ManyToOne
    @JoinColumn(name = "sender_wallet_id")
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id")
    private Wallet receiverWallet;

    @Column(nullable = false)
    String note;

    protected Transfer() {}

    public Transfer(Double amount, TransactionType type, Wallet senderWallet, Wallet receiverWallet, String userPosition, String note) {
        super(amount, "",
              userPosition.equals("sender") ? senderWallet : receiverWallet,
              type);
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.note = note;
        String formattedDescription = "";
        if (type == TransactionType.TRANSFER) {
            formattedDescription = String.format("Transfer %.2f from %s to %s",
                    amount,
                    senderWallet.getUser().getName(),
                    receiverWallet.getUser().getName());
        } else if (type == TransactionType.TIP) {
            formattedDescription = String.format("Tip %.2f from %s to %s",
                    amount,
                    senderWallet.getUser().getName(),
                    receiverWallet.getUser().getName());
        }
        this.setDescription(formattedDescription);
    }
}