package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transaction;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransactionType;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.TransactionRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.TopUpStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TopUpService {
    private final Map<String, TopUpStrategy> strategies;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TopUpService(List<TopUpStrategy> strategyList,
                        WalletRepository walletRepository,
                        TransactionRepository transactionRepository) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        TopUpStrategy::getProviderName,
                        strategy -> strategy
                ));
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public boolean topUp(TopUpRequest request) {
        String providerName = request.getPaymentGatewayDetails().get("provider");
        if (providerName == null) {
            throw new IllegalArgumentException("Provider not specified in payment gateway details");
        }

        TopUpStrategy strategy = strategies.get(providerName);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported top-up method: " + providerName);
        }

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        boolean success = strategy.processTopUp(wallet, request);

        if (success) {
            Transaction transaction = new Transaction();
            transaction.setWallet(wallet);
            transaction.setAmount(request.getAmount());
            transaction.setType(TransactionType.TOPUP);
            transaction.setDescription("Top-up via " + providerName);
            transaction.setProvider(providerName);

            transactionRepository.save(transaction);
            walletRepository.save(wallet);
        }

        return success;
    }
}