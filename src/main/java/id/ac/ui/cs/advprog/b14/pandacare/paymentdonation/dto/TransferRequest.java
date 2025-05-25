package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private String receiverEmail;
    private Double amount;
    private String note;
}