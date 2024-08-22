package com.example.exchange.Services.Interfaces;


import com.example.exchange.Models.DTOs.AccountResponse;

public interface AccountService {
    AccountResponse getAccountByUsername(String email);

    AccountResponse getCurrentAccount();

}
