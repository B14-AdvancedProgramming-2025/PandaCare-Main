package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.listener;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.event.UserCreatedEvent;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserEventListenerTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private UserEventListener userEventListener;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @Test
    void testHandleUserCreatedEvent() {
        // Arrange
        User user = new Pacilian(
                "test-id",
                "test@example.com",
                "password123",
                "Test User",
                "1234567890123456",
                "Test Address",
                "08123456789",
                Arrays.asList("None")
        );
        UserCreatedEvent event = new UserCreatedEvent(this, user);

        userEventListener.handleUserCreatedEvent(event);

        verify(walletRepository).save(walletCaptor.capture());

        Wallet savedWallet = walletCaptor.getValue();
        assertNotNull(savedWallet);
        assertEquals(user, savedWallet.getUser());
        assertEquals(0.0, savedWallet.getBalance());
        assertTrue(savedWallet.getTransactions().isEmpty());

        assertEquals(savedWallet, user.getWallet());
    }
}