package com.example.wallet.Services.Interfaces;

import com.example.wallet.Models.Token;
import com.example.wallet.Models.Wallet;

import java.math.BigDecimal;

public interface TokenService {


    void deleteToken(Long id);

    Token findTokenById(Long id);
    Token findTokenBySymbolAndWallet(String type, Wallet wallet);
    void transferTokens(String type, BigDecimal amount, Wallet sourceWallet , Wallet destinationWallet);

    void addTokens(String tokenType, BigDecimal amount, Wallet wallet);

    void subtractTokens(String tokenType, BigDecimal amount, Wallet wallet);
}
