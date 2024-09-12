package com.example.exchange.Services;

import com.example.exchange.Client.AuthenticationClient;
import com.example.exchange.Models.DTOs.AccountResponse;
import com.example.exchange.Services.Interfaces.AccountService;
import com.example.exchange.Services.Interfaces.RedisAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AuthenticationClient authenticationClient;
    private final RedisAccountService redisAccountService;

    @Override
    public Long findAccountIdByEmail(String email) {
        return redisAccountService.getAccountId(email).orElseGet(() -> {
            AccountResponse accountResponse = authenticationClient.findAccountByEmail(email);
            Long accountId = accountResponse.id();
            redisAccountService.saveAccount(accountResponse);
            return accountId;
        });
    }



}
