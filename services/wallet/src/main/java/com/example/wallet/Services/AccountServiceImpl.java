package com.example.wallet.Services;

import com.example.wallet.Client.AuthenticationClient;
import com.example.wallet.Models.DTO.AccountResponse;
import com.example.wallet.Services.Interfaces.AccountService;
import com.example.wallet.Utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AuthenticationClient authenticationClient;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public AccountResponse getAccountByUsername(String username) {
        return authenticationClient.findAccountByEmail(username);
    }

    @Override
    public AccountResponse getCurrentAccount() {
        String username = getCurrentUsername();
        return getAccountByUsername(username);
    }

    private String getCurrentUsername() {
        String jwt = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return jwtTokenUtils.getUsername(jwt);
    }
}
