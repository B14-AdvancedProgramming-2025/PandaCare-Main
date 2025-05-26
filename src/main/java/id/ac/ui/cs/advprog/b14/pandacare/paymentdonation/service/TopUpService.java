package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUp;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.repository.WalletRepository;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.BankTransferTopUpStrategy;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.CreditCardTopUpStrategy;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy.TopUpStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class TopUpService {
    private final WalletRepository walletRepository;

    public TopUpService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> processTopUp(User user, TopUpRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request.getAmount() <= 0) {
            response.put("success", false);
            response.put("message", "Amount must be greater than zero");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Wallet wallet = walletRepository.findByUser(user);
        if (wallet == null) {
            response.put("success", false);
            response.put("message", "Wallet not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        TopUpStrategy strategy;

        if ("BANK_TRANSFER".equalsIgnoreCase(request.getMethod())) {
            if (request.getBankName() == null || request.getBankName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Bank name is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (request.getAccountNumber() == null || request.getAccountNumber().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Account number is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            strategy = new BankTransferTopUpStrategy(
                    request.getBankName(),
                    request.getAccountNumber()
            );
        } else if ("CREDIT_CARD".equalsIgnoreCase(request.getMethod())) {
            if (request.getCardNumber() == null || !isValidCardNumber(request.getCardNumber())) {
                response.put("success", false);
                response.put("message", "Invalid card number");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (request.getExpiryDate() == null || isCardExpired(request.getExpiryDate())) {
                response.put("success", false);
                response.put("message", "Card is expired");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }

            if (request.getCvv() == null || !isValidCvv(request.getCvv())) {
                response.put("success", false);
                response.put("message", "Invalid CVV");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            strategy = new CreditCardTopUpStrategy(
                    request.getCardNumber(),
                    request.getCvv(),
                    request.getExpiryDate(),
                    request.getCardholderName()
            );
        } else {
            response.put("success", false);
            response.put("message", "Invalid method");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            TopUp topUp = strategy.execute(wallet, request.getAmount());
            if (topUp == null) {
                response.put("success", false);
                response.put("message", "Failed to process top-up");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            walletRepository.save(wallet);
            response.put("success", true);
            response.put("message", "Topped-Up successfully");
            Map<String, Object> data = new HashMap<>();
            data.put("amount", topUp.getAmount());
            data.put("balance", wallet.getBalance());
            data.put("provider", topUp.getProvider());
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Payment processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        String cleaned = cardNumber.replaceAll("\\s+", "");
        return cleaned.matches("\\d{16}");
    }

    private boolean isCardExpired(String expiryDate) {
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;

            Calendar expiry = Calendar.getInstance();
            expiry.set(year, month - 1, 1);

            Calendar now = Calendar.getInstance();
            return expiry.before(now);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean isValidCvv(String cvv) {
        return cvv.matches("\\d{3,4}");
    }
}
