package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.containers.AccountContainer;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountContainer<Account> registerAccount(Account account) {

        AccountContainer<Account> newAccount = new AccountContainer<>();

        String username = account.getUsername();
        String password = account.getPassword();

        if (username == "" || password.length() < 4) {
            newAccount.setErrorMessage("Invalid username or Password");
            newAccount.setErrorCode(400);
            return newAccount;
        }

        if (accountRepository.existsByUsername(username)) {
            newAccount.setErrorMessage("Username Already Exists");
            newAccount.setErrorCode(409);
            return newAccount;
        }

        newAccount.setValue(accountRepository.save(account));

        return newAccount;

    }

    public AccountContainer<Account> logIn(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        AccountContainer<Account> loggedAccount = new AccountContainer<>();

        Account res = accountRepository.findByUsernameAndPassword(username, password);

        if (res == null) {
            loggedAccount.setErrorMessage("Invalid Username or Password");
            loggedAccount.setErrorCode(401);
        } else {
            loggedAccount.setValue(res);
        }

        return loggedAccount;

    }
}
