package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.strategy;

import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.TopUpRequest;
import id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model.Wallet;

public interface TopUpStrategy {
    boolean processTopUp(Wallet wallet, TopUpRequest request);
    String getProviderName();
}