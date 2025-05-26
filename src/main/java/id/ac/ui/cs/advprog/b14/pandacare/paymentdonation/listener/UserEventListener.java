package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.listener;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.event.UserCreatedEvent;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserEventListener {

    private final WalletRepository walletRepository;

    public UserEventListener(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @EventListener
    @Transactional
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        User user = event.getUser();
        Wallet wallet = new Wallet(user);
        user.setWallet(wallet);
        walletRepository.save(wallet);
    }
}