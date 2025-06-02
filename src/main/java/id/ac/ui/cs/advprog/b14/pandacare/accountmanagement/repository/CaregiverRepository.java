package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<Caregiver, String> {
    Optional<Caregiver> findByEmail(String email);
}