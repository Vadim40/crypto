package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.AccountNotFoundException;
import com.example.authenticationservice.Exceptions.PasswordNotMatchException;
import com.example.authenticationservice.Kafka.AuthenticationProducer;
import com.example.authenticationservice.Kafka.DTOs.PasswordChangeEvent;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Kafka.DTOs.AccountCreationEvent;
import com.example.authenticationservice.Models.Enums.Role;
import com.example.authenticationservice.Repositories.AccountRepository;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProducer authenticationProducer;

    private final CustomUserDetailsService userDetailsService;

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Override
    public Account saveAccount(Account account) {
        account.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account savedAccount= accountRepository.save(account);
        sendAccountCreationEvent(savedAccount);
        return  savedAccount;
    }

    private void sendAccountCreationEvent(Account account) {
        AccountCreationEvent accountCreationEvent = new AccountCreationEvent(account.getEmail(), account.getId());
        authenticationProducer.sendAccountCreationConfirmation(accountCreationEvent);
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
        Account account = userDetailsService.getAuthenticatedUser();
        account.setOtpEnabled(true);
        accountRepository.save(account);
    }

    @Override
    public void disable2fa() {
        Account account = userDetailsService.getAuthenticatedUser();
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
    public void changePassword(String newPassword, String ip) {
        Account account = userDetailsService.getAuthenticatedUser();
        account.setPassword(passwordEncoder.encode(newPassword));
        saveAccount(account);
        sendPasswordChangeEvent(account, ip);
    }

    private void sendPasswordChangeEvent(Account account, String ip) {
        PasswordChangeEvent passwordChangeEvent=new PasswordChangeEvent(account.getId(),account.getEmail(), LocalDateTime.now(),ip);
        authenticationProducer.sendPasswordChangeEvent(passwordChangeEvent);
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
