package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.AccountNotFoundException;
import com.example.authenticationservice.Exceptions.PasswordNotMatchException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.Enums.Role;
import com.example.authenticationservice.Repositories.AccountRepository;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService userDetailsService;
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
    public void enable2fa() {
        Account account =userDetailsService.getAuthenticatedUser();
        account.setOtpEnabled(true);
        accountRepository.save(account);
    }

    @Override
    public void disable2fa() {
        Account account =userDetailsService.getAuthenticatedUser();
        account.setOtpEnabled(false);
        accountRepository.save(account);
    }

    @Override
    public void comparePassword(String oldPassword) {
        Account account = userDetailsService.getAuthenticatedUser();
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new PasswordNotMatchException("old password doesn't match");
        }
    }

    @Override
    public void changePassword(String newPassword) {
        Account account = userDetailsService.getAuthenticatedUser();
        account.setPassword(passwordEncoder.encode(newPassword));
        saveAccount(account);
    }

    @Override
    public boolean isEmailExists(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public void deleteAllAccounts() {
        accountRepository.deleteAll();
    }

    @Override
    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }


}
