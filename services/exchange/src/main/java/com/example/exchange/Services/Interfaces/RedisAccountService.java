package com.example.exchange.Services.Interfaces;



import com.example.exchange.Models.DTOs.AccountResponse;

import java.util.Optional;

public interface RedisAccountService {
    void saveAccount(AccountResponse accountResponse);
    Optional<Long> getAccountId(String email);
    void deleteAccountByEmail(String email);
}
