package com.example.exchange.Services.Interfaces;


import com.example.exchange.Models.DTOs.AccountResponse;

public interface AccountService {
    AccountResponse findAccountByEmail(String email);

}
