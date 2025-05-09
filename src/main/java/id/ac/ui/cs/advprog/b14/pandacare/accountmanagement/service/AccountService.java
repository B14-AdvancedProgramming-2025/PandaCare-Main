package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;

import java.util.UUID;

public interface AccountService {
    Account getAccountById(UUID id);
    Account updateAccount(Account account);
    void deleteAccount(UUID id);
}
