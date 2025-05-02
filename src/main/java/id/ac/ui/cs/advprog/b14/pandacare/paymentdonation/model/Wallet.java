package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    protected Wallet() {}

    public Wallet(User user) {
        this.user = user;
    }

    public boolean deductBalance(Double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    public boolean addBalance(Double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }
}