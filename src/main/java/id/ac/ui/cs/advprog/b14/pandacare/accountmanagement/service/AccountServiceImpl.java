package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.service;

import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.model.Account;
import id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.NoSuchElementException;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account not found"));
    }

    @Override
    public Account updateAccount(Account account) {
        if (!accountRepository.existsById(account.getId())) {
            throw new IllegalArgumentException("Account not found");
        }
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Account not found");
        }
        accountRepository.deleteById(id);
    }

}
