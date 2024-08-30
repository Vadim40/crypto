package com.example.authenticationservice.Repositories;

import com.example.authenticationservice.Models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByTokenHash(String tokenHash);

    void deleteRefreshTokenByAccountEmail(String email);
    void deleteByExpiryTimeBefore(LocalDateTime expiryTime);

}
