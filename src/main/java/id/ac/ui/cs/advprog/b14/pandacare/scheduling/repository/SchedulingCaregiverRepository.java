package id.ac.ui.cs.advprog.b14.pandacare.scheduling.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulingCaregiverRepository extends JpaRepository<Caregiver, String> {
    // Standard JPA methods will be inherited
}