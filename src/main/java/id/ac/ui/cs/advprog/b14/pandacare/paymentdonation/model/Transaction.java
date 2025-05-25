package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name= "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    protected Transaction() {}

    public Transaction(Double amount, String description, Wallet wallet, TransactionType type) {
        this.amount = amount;
        this.description = description;
        this.wallet = wallet;
        this.type = type;
    }
}