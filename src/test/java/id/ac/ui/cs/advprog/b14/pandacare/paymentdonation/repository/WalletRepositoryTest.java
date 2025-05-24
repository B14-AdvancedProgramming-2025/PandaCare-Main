package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WalletRepository walletRepository;

    private User user;

    @BeforeEach
    void setUp() {
        entityManager.clear();
        user = new Pacilian(
                "pacil-123",
                "test@example.com",
                "password123",
                "Test User",
                "1234567890123456",
                "123 Test Street",
                "08123456789",
                Arrays.asList("None")
        );
        entityManager.persist(user);
    }

    @Test
    void testSaveWallet() {
        Wallet wallet = new Wallet(user);
        wallet.setBalance(100.0);

        Wallet savedWallet = walletRepository.save(wallet);

        assertNotNull(savedWallet.getId());
        assertEquals(100.0, savedWallet.getBalance());
        assertEquals(user, savedWallet.getUser());
    }

    @Test
    void testFindByUser() {
        Wallet wallet = new Wallet(user);
        wallet.setBalance(150.0);

        entityManager.persistAndFlush(wallet);

        Wallet foundWallet = walletRepository.findByUser(user);

        assertNotNull(foundWallet);
        assertEquals(user, foundWallet.getUser());
        assertEquals(150.0, foundWallet.getBalance());
    }

    @Test
    void testUpdateWallet() {
        Wallet wallet = new Wallet(user);
        wallet.setBalance(200.0);

        Wallet savedWallet = entityManager.persistAndFlush(wallet);

        savedWallet.setBalance(300.0);
        walletRepository.save(savedWallet);

        Wallet updatedWallet = entityManager.find(Wallet.class, savedWallet.getId());

        assertEquals(300.0, updatedWallet.getBalance());
    }

    @Test
    void testDeleteWallet() {
        Wallet wallet = new Wallet(user);

        Wallet savedWallet = entityManager.persistAndFlush(wallet);

        walletRepository.deleteById(savedWallet.getId());

        Wallet deletedWallet = entityManager.find(Wallet.class, savedWallet.getId());

        assertNull(deletedWallet);
    }
}