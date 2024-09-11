package com.example.exchange.Services;

import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Services.Interfaces.CryptoRateRedisService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service

public class CryptoRateRedisServiceImpl implements CryptoRateRedisService {

    private final RedisTemplate<String,String> redisTemplate;
    private static final String PREFIX="cryptoRate:";

    public CryptoRateRedisServiceImpl( @Qualifier("redisTemplateString") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveCryptoRate(CryptoRate cryptoRate) {
        String key = PREFIX + cryptoRate.getBaseCurrency() + ":" + cryptoRate.getTargetCurrency();
        redisTemplate.opsForValue().set(key, cryptoRate.getRate().toPlainString(), 2, TimeUnit.HOURS);
    }

    @Override
    public Optional<BigDecimal> findCryptoRate(String baseCurrency, String targetCurrency) {
        String key = PREFIX +baseCurrency+":"+targetCurrency ;
        BigDecimal rate=new BigDecimal(redisTemplate.opsForValue().get(key));
        return Optional.of(rate);
    }

    public void deleteAllCryptoRates() {
        String pattern = PREFIX + "*";
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
