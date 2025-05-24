package id.ac.ui.cs.advprog.b14.pandacare.authentication.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}