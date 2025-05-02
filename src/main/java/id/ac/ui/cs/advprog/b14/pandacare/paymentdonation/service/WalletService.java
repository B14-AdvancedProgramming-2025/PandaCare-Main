package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.*;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.TransactionRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Wallet createWalletForUser(User user) {
        Wallet wallet = new Wallet(user);
        return walletRepository.save(wallet);
    }

    @Transactional
    public boolean makePayment(Long walletId, Double amount, String purpose) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        boolean success = wallet.deductBalance(amount);
        if (success) {
            Transaction transaction = new Transaction();
            transaction.setWallet(wallet);
            transaction.setAmount(-amount);
            transaction.setType(TransactionType.PAYMENT);
            transaction.setDescription(purpose);

            transactionRepository.save(transaction);
            walletRepository.save(wallet);
        }
        return success;
    }

    public Double getBalance(Long walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
    }

    public List<Transaction> getTransactionHistory(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
        return transactionRepository.findByWalletOrderByTimestampDesc(wallet);
    }
}