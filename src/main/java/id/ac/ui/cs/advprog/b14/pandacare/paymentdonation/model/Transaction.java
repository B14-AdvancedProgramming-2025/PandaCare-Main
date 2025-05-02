package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime timestamp = LocalDateTime.now();
    private String provider;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}