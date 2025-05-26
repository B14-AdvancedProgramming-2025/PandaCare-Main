package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto.TransferRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class TransactionController {
    private final WalletService walletService;

    public TransactionController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance(
            @AuthenticationPrincipal User user) {
        return walletService.getBalance(user);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferFunds(
            @AuthenticationPrincipal User user,
            @RequestBody TransferRequest request) {
        return walletService.transferFunds(user, request);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Map<String, Object>> getTransactionHistory(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return walletService.getTransactionHistory(user, page, size);
    }
}