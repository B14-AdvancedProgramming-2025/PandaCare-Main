package id.ac.ui.cs.advprog.b14.pandacare.authentication.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByToken(String token);

    void deleteByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expiryDate < ?1")
    void deleteExpiredTokens(Date now);
}