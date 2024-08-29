package com.example.authenticationservice.Services.Interfaces;

import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Account account);
    void validateRefreshToken(Account account, String token);
    RefreshToken findByAccount(Account account);
    void deleteTokenByAccount(Account account);




}
