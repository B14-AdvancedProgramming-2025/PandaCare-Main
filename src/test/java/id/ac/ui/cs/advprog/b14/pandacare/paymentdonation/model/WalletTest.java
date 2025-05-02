package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Mock
    private User mockUser;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        wallet = new Wallet(mockUser);
    }

    @Test
    void testDefaultConstructor() {
        assertNull(wallet.getId());
        assertEquals(mockUser, wallet.getUser());
        assertEquals(0.0, wallet.getBalance());
    }

    @Test
    void testAddAndDeductBalance() {
        wallet.addBalance(100.0);
        assertEquals(100.0, wallet.getBalance());

        boolean result = wallet.addBalance(-10.0);
        assertFalse(result);

        result = wallet.deductBalance(50.0);
        assertTrue(result);
        assertEquals(50.0, wallet.getBalance());

        result = wallet.deductBalance(60.0);
        assertFalse(result);
        assertEquals(50.0, wallet.getBalance());
    }
}