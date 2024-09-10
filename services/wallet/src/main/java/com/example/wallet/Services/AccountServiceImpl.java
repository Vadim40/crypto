package com.example.wallet.Services;

import com.example.wallet.Client.AuthenticationClient;
import com.example.wallet.Models.DTO.AccountResponse;
import com.example.wallet.Services.Interfaces.AccountService;
import com.example.wallet.Services.Interfaces.RedisAccountService;
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
