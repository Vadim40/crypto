package com.example.wallet.Services.Interfaces;

import com.example.wallet.Models.DTO.AccountResponse;

import java.util.Optional;

public interface RedisAccountService {
    void saveAccount(AccountResponse accountResponse);
    Optional<Long> getAccountId(String email);
    void deleteAccountByEmail(String email);
}
