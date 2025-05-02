package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;

public interface AccountService {
    Account getAccountById(String id);
    Account updateAccount(Account account);
    void deleteAccount(String id);
}
