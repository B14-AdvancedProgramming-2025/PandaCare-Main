package id.ac.ui.cs.advprog.b14.pandacare.authentication.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TokenRepository tokenRepository;

    private Token validToken;

    @BeforeEach
    void setUp() {
        tokenRepository.deleteAll();

        Calendar futureCalendar = Calendar.getInstance();
        futureCalendar.add(Calendar.HOUR, 1);
        Date futureDate = futureCalendar.getTime();
        validToken = new Token("valid-token-string", futureDate);

        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.add(Calendar.HOUR, -1);
        Date pastDate = pastCalendar.getTime();
        Token expiredToken = new Token("expired-token-string", pastDate);

        entityManager.persist(validToken);
        entityManager.persist(expiredToken);
        entityManager.flush();
    }

    @Test
    void testFindByToken() {
        Token found = tokenRepository.findByToken("valid-token-string");

        assertNotNull(found);
        assertEquals("valid-token-string", found.getToken());
        assertEquals(validToken.getExpiryDate(), found.getExpiryDate());
    }

    @Test
    void testFindByNonExistentToken() {
        Token found = tokenRepository.findByToken("non-existent-token");

        assertNull(found);
    }

    @Test
    void testDeleteByToken() {
        tokenRepository.deleteByToken("valid-token-string");

        Token found = tokenRepository.findByToken("valid-token-string");
        assertNull(found);

        Token expiredFound = tokenRepository.findByToken("expired-token-string");
        assertNotNull(expiredFound);
    }

    @Test
    void testDeleteExpiredTokens() {
        tokenRepository.deleteExpiredTokens(new Date());

        Token validFound = tokenRepository.findByToken("valid-token-string");
        Token expiredFound = tokenRepository.findByToken("expired-token-string");

        assertNotNull(validFound);
        assertNull(expiredFound);
    }

    @Test
    void testFindAll() {
        List<Token> allTokens = tokenRepository.findAll();

        assertEquals(2, allTokens.size());
    }
}