package com.example.authenticationservice.Services.Interfaces;

import com.example.authenticationservice.Models.Account;

public interface AccountService {
    Account findAccountByEmail(String email);

    Account saveAccount(Account account);

    Account updateAccount(Account account, String email);

    void deleteAccount(String email);

    void enable2fa();

    void disable2fa();

    void comparePassword(String oldPassword);

    void changePassword(String newPassword, String ip);

    boolean isEmailExists(String email);

    void deleteAllAccounts();

    Account findAccountById(Long accountId);
}
