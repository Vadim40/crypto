package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.AccountNotFoundException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.Enums.Role;
import com.example.authenticationservice.Repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Override
    public Account saveAccount(Account account) {
        account.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account, String email) {
        account.setId(findAccountByEmail(email).getId());
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(String email) {
        accountRepository.deleteAccountByEmail(email);
    }

    @Override
    public void enableTfa(String email) {
        Account account=findAccountByEmail(email);
        account.setOTPEnabled(true);
        accountRepository.save(account);
    }


}
