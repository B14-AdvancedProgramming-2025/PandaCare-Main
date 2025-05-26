package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopUpRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Method is required")
    private String method;

    private String bankName;
    private String accountNumber;

    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardholderName;
}