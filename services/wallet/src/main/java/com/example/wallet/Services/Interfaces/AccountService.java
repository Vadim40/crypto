package com.example.wallet.Services.Interfaces;

import com.example.wallet.Models.DTO.AccountResponse;

public interface AccountService {
    AccountResponse getAccountByUsername(String email);

    AccountResponse getCurrentAccount();
}
