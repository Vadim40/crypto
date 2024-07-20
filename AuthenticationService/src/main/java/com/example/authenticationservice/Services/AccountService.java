package com.example.authenticationservice.Services;

import com.example.authenticationservice.Models.Account;

public interface AccountService {
    Account findAccountByEmail(String email);

    Account saveAccount(Account account);

    Account updateAccount(Account account, String email);

    void deleteAccount(String email);

}
