package com.example.walletservice.Services;

import com.example.walletservice.Models.Account;
import com.example.walletservice.Repositories.AccountRepository;

public interface AccountService {
    Account findAccountByEmail(String email);

    Account saveAccount(Account account);

    Account updateAccount(Account account, String email);

    void deleteAccount(String email);

}
