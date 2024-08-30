package com.example.authenticationservice.Services.Interfaces;

import com.example.authenticationservice.Models.RefreshToken;

public interface RefreshTokenService {
    String createRefreshToken(String email);
    String validateRefreshTokenReturnEmail(String token);
    RefreshToken findRefreshTokenByToken(String email);
    void deleteTokenByAccountEmail(String email);




}
