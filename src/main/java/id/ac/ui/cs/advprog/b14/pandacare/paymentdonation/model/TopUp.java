package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name= "topups")
@Getter @Setter
public class TopUp extends Transaction {
    @Column(nullable = false)
    private String provider;

    protected TopUp() {}

    public TopUp(Double amount, Wallet wallet, TransactionType type, String provider) {
        super(amount, "", wallet, type);
        this.provider = provider;

        String formattedDescription = String.format("TopUp %.2f from %s",
                amount,
                provider);
        this.setDescription(formattedDescription);
    }
}