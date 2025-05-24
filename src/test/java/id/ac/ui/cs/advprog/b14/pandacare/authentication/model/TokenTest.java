package id.ac.ui.cs.advprog.b14.pandacare.authentication.model;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    void testTokenConstructor() {
        String tokenString = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0";
        Date expiryDate = new Date();

        Token token = new Token(tokenString, expiryDate);

        assertEquals(tokenString, token.getToken());
        assertEquals(expiryDate, token.getExpiryDate());
    }

    @Test
    void testTokenSettersAndGetters() {
        Token token = new Token();
        String tokenString = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0";
        Date expiryDate = new Date();

        token.setToken(tokenString);
        token.setExpiryDate(expiryDate);

        assertEquals(tokenString, token.getToken());
        assertEquals(expiryDate, token.getExpiryDate());
    }

    @Test
    void testIsExpiredWithExpiredToken() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1); // 1 hour in the past
        Date pastDate = calendar.getTime();

        Token expiredToken = new Token("expired-token", pastDate);

        assertTrue(expiredToken.isExpired());
    }

    @Test
    void testIsExpiredWithValidToken() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1); // 1 hour in the future
        Date futureDate = calendar.getTime();

        Token validToken = new Token("valid-token", futureDate);

        assertFalse(validToken.isExpired());
    }
}