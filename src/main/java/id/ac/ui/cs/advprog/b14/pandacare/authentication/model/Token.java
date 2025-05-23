package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter @Setter
@NoArgsConstructor
public class Token {
    @Id
    private String token;

    @Column(nullable = false)
    private Date expiryDate;

    public Token(String token, Date expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.before(new Date());
    }
}