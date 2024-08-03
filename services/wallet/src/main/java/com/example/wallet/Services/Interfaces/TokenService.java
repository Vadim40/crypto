package com.example.wallet.Services.Interfaces;

import com.example.wallet.Models.Token;

public interface TokenService {
    Token saveToken(Token token);

    Token updateToken(Token token, Long id);

    void deleteToken(Long id);

    Token findTokenById(Long id);
}
