package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.controller;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.PaymentRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TransferRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final WalletService walletService;

    public TransactionController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> processPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            boolean success = walletService.makePayment(
                    request.getWalletId(),
                    request.getAmount(),
                    "Payment to " + request.getMerchantId() + " (Invoice: " + request.getInvoiceNumber() + ")"
            );

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Payment processed successfully",
                        "amount", request.getAmount(),
                        "merchantId", request.getMerchantId(),
                        "invoiceNumber", request.getInvoiceNumber()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "Insufficient funds for payment"
                ));
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> processTransfer(@Valid @RequestBody TransferRequest request) {
        try {
            boolean success = walletService.Transfer(
                    request.getWalletId(),
                    request.getRecipientWalletId(),
                    request.getAmount()
            );

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Transfer processed successfully",
                        "amount", request.getAmount(),
                        "recipientWalletId", request.getRecipientWalletId(),
                        "reference", request.getTransferReference()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "success", false,
                        "message", "Insufficient funds for transfer"
                ));
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}