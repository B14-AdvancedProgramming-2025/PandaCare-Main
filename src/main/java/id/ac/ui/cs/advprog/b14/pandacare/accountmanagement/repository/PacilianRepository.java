package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacilianRepository extends JpaRepository<Pacilian, String> {
    Optional<Pacilian> findByEmail(String email);
}