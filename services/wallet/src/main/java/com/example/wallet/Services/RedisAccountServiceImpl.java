package com.example.wallet.Services;

import com.example.wallet.Models.DTO.AccountResponse;
import com.example.wallet.Services.Interfaces.RedisAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisAccountServiceImpl implements RedisAccountService {
    private final RedisTemplate<String,Long> redisTemplate;
    @Override
    public void saveAccount(AccountResponse accountResponse) {
        redisTemplate.opsForValue().set("account:" + accountResponse.email(), accountResponse.id(), 1, TimeUnit.DAYS);
    }

    @Override
    public Optional<Long> getAccountId(String email) {

        Object accountId = redisTemplate.opsForValue().get("account:" + email);
        if (accountId instanceof Long) {
            return Optional.of((Long) accountId);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAccountByEmail(String email) {
        redisTemplate.delete("account:" + email);
    }
}
