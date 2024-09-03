package com.example.wallet.Services;

import com.example.wallet.Client.AuthenticationClient;
import com.example.wallet.Models.DTO.AccountResponse;
import com.example.wallet.Services.Interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AuthenticationClient authenticationClient;

    @Override
    public AccountResponse findAccountByEmail(String email) {
        return authenticationClient.findAccountByEmail(email);
    }


}
