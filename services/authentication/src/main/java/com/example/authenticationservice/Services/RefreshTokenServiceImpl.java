package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.RefreshTokenExpiredException;
import com.example.authenticationservice.Exceptions.RefreshTokenNotFoundException;
import com.example.authenticationservice.Exceptions.RefreshTokenValidationException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.RefreshToken;
import com.example.authenticationservice.Repositories.RefreshTokenRepository;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import com.example.authenticationservice.Services.Interfaces.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountService accountService;
    @Value("${jwt.lifetime.refresh}")
    private Duration refreshLifeTime;
    @Transactional
    @Override
    public String createRefreshToken(String email) {
        Account account = accountService.findAccountByEmail(email);
        String token = generateRefreshToken();
        saveRefreshToken(account, token);
        return token;
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }


    private void saveRefreshToken(Account account, String token) {
        String tokenHash = hashToken(token);
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByAccount(account)
                .orElse(new RefreshToken());
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setAccount(account);
        refreshToken.setExpiryTime(LocalDateTime.now().plus(refreshLifeTime));
        refreshTokenRepository.save(refreshToken);
        account.setRefreshToken(refreshToken);
        accountService.saveAccount(account);
    }


    @Override
    public String validateRefreshTokenReturnEmail(String token) {
        RefreshToken refreshToken = findRefreshTokenByToken(token);
        compareTokens(refreshToken, token);
        checkExpiredTime(refreshToken);
        return refreshToken.getAccount().getEmail();
    }

    private void compareTokens(RefreshToken refreshToken, String token) {
        String tokenHash = hashToken(token);
        if (!refreshToken.getTokenHash().equals(tokenHash)) {
            throw new RefreshTokenValidationException("Invalid refresh token");
        }
    }

    private void checkExpiredTime(RefreshToken refreshToken) {
        if (refreshToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException("Expire refresh token");
        }
    }

    @Override
    public RefreshToken findRefreshTokenByToken(String token) {
        String hashToken = hashToken(token);
        return refreshTokenRepository.findRefreshTokenByTokenHash(hashToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
    }

    private String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }

    @Override
    @Transactional
    public void deleteTokenByAccountEmail(String email) {
        refreshTokenRepository.deleteRefreshTokenByAccountEmail(email);
        Account account = accountService.findAccountByEmail(email);
        account.setRefreshToken(null);
        accountService.saveAccount(account);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 36000000)
    public void deleteExpiredTokens() {
        LocalDateTime now=LocalDateTime.now();
        refreshTokenRepository.deleteByExpiryTimeBefore(now);
    }
}
