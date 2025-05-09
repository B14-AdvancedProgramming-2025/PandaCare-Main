package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Account {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nik;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @ElementCollection
    @CollectionTable(name = "account_medical_history", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "medical_history")
    private List<String> medicalHistory;

    private String specialty;

    @ElementCollection
    @CollectionTable(name = "account_working_schedule", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "working_schedule")
    private List<String> workingSchedules;
}
