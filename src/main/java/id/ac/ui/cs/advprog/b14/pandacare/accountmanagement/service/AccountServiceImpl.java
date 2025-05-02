package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    // Dummy database
    private static final Map<String, Account> accountDatabase = new HashMap<>();

    @Override
    public Account getAccountById(String id) {
        return accountDatabase.get(id);
    }

    @Override
    public Account updateAccount(Account account) {
        if (!accountDatabase.containsKey(account.getId().toString())) {
            throw new IllegalArgumentException("Account not found");
        }

        accountDatabase.put(account.getId().toString(), account);
        return account;
    }

    @Override
    public void deleteAccount(String id) {
        if (!accountDatabase.containsKey(id)) {
            throw new IllegalArgumentException("Account not found");
        }
        accountDatabase.remove(id);
    }
}
