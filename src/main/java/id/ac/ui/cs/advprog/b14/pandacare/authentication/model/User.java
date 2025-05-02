package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public abstract class User {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "NIK cannot be empty")
    private String nik;

    @Column(nullable = false)
    @NotBlank(message = "Address cannot be empty")
    private String address;

    @Column(nullable = false)
    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    protected User() {}

    public User(String email, String password, String name, String nik, String address, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nik = nik;
        this.address = address;
        this.phone = phone;
    }
}