package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
