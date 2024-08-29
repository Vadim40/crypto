package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.RefreshTokenExpiredException;
import com.example.authenticationservice.Exceptions.RefreshTokenNotFoundException;
import com.example.authenticationservice.Exceptions.RefreshTokenValidationException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.RefreshToken;
import com.example.authenticationservice.Repositories.RefreshTokenRepository;
import com.example.authenticationservice.Services.Interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.lifetime.refresh}")
    private Duration refreshLifeTime;

    @Override
    public RefreshToken createRefreshToken(Account account) {
        RefreshToken refreshToken = new RefreshToken();
        String token = UUID.randomUUID().toString();
        refreshToken.setToken(token);
        refreshToken.setAccount(account);
        refreshToken.setExpiryDate(LocalDateTime.now().plus(refreshLifeTime));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(Account account, String token) {
        RefreshToken refreshToken = findByAccount(account);
        compareTokens(refreshToken, token, account);
        checkExpiredTime(refreshToken, account);
    }

    private void compareTokens(RefreshToken refreshToken, String token, Account account) {

        if (!refreshToken.getToken().equals(token)) {
            throw new RefreshTokenValidationException("Refresh token for this account:" + account + " not found");
        }
    }

    private void checkExpiredTime(RefreshToken refreshToken, Account account) {
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException("Refresh token for this account:" + account + " is expired");
        }
    }

    @Override
    public RefreshToken findByAccount(Account account) {
        return refreshTokenRepository.findRefreshTokenByAccount(account)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token for this account:" + account + " not found"));
    }

    @Override
    public void deleteTokenByAccount(Account account) {
        refreshTokenRepository.deleteRefreshTokenByAccount(account);
    }
}
