package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TransferRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transaction;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Transfer;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.TransactionRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.TransferStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository,
                         TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public ResponseEntity<Map<String, Object>> getBalance(User user) {
        Map<String, Object> response = new HashMap<>();

        Wallet wallet = walletRepository.findByUser(user);
        if (wallet == null) {
            response.put("success", false);
            response.put("message", "Wallet not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("success", true);
        response.put("message", "Balance retrieved successfully");
        Map<String, Object> data = new HashMap<>();
        data.put("balance", wallet.getBalance());
        response.put("data", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> transferFunds(User sender, TransferRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Wallet senderWallet = walletRepository.findByUser(sender);
            if (senderWallet == null) {
                response.put("success", false);
                response.put("message", "Sender wallet not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User receiver = userRepository.findByEmail(request.getReceiverEmail());
            if (receiver == null) {
                response.put("success", false);
                response.put("message", "Receiver not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Wallet receiverWallet = walletRepository.findByUser(receiver);
            if (receiverWallet == null) {
                response.put("success", false);
                response.put("message", "Receiver wallet not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if (senderWallet.equals(receiverWallet)) {
                response.put("success", false);
                response.put("message", "Cannot transfer to the same wallet");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (request.getAmount() <= 0) {
                response.put("success", false);
                response.put("message", "Transfer amount must be positive");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (senderWallet.getBalance() < request.getAmount()) {
                response.put("success", false);
                response.put("message", "Insufficient balance");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            TransferStrategy strategy = new TransferStrategy(receiverWallet, request.getNote());
            Transfer transfer = strategy.execute(senderWallet, request.getAmount());

            if (transfer == null) {
                response.put("success", false);
                response.put("message", "Failed to process transfer");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);

            response.put("success", true);
            response.put("message", "Transfer successful");
            Map<String, Object> data = new HashMap<>();
            data.put("sender", sender.getName());
            data.put("senderBalance", senderWallet.getBalance());
            data.put("receiver", receiver.getName());
            data.put("receiverBalance", receiverWallet.getBalance());
            data.put("amount", transfer.getAmount());
            data.put("note", transfer.getNote());
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Transfer processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getTransactionHistory(User user, int page, int size) {
        Map<String, Object> response = new HashMap<>();

        Wallet wallet = walletRepository.findByUser(user);
        if (wallet == null) {
            response.put("success", false);
            response.put("message", "Wallet not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Use Spring Data JPA Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Transaction> transactionsPage = transactionRepository.findByWallet(wallet, pageable);

        List<Map<String, Object>> transactionDataList = transactionsPage.getContent().stream()
                .map(this::mapTransactionToResponse)
                .toList();

        response.put("success", true);
        response.put("message", "Transaction history retrieved successfully");
        response.put("transactions", transactionDataList);
        response.put("currentPage", transactionsPage.getNumber());
        response.put("totalItems", transactionsPage.getTotalElements());
        response.put("totalPages", transactionsPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private Map<String, Object> mapTransactionToResponse(Transaction transaction) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", transaction.getId());
        data.put("amount", transaction.getAmount());
        data.put("description", transaction.getDescription());
        data.put("type", transaction.getType().toString());
        data.put("timestamp", transaction.getTimestamp());
        // Add specialized fields based on transaction type
        if (transaction instanceof TopUp) {
            data.put("provider", ((TopUp) transaction).getProvider());
        } else if (transaction instanceof Transfer) {
            data.put("note", ((Transfer) transaction).getNote());
            data.put("senderName", ((Transfer) transaction).getSenderWallet().getUser().getName());
            data.put("receiverName", ((Transfer) transaction).getReceiverWallet().getUser().getName());
        }
        return data;
    }
}