package com.example.exchange.Services;

import com.example.exchange.Client.AuthenticationClient;
import com.example.exchange.Models.DTOs.AccountResponse;
import com.example.exchange.Services.Interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AuthenticationClient authenticationClient;

    @Override
    public AccountResponse findAccountByEmail(String email) {
        return authenticationClient.findAccountByEmail(email);
    }

}
