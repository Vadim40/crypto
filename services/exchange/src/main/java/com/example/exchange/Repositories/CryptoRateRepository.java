package com.example.exchange.Repositories;

import com.example.exchange.Models.CryptoRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoRateRepository extends JpaRepository<CryptoRate, Long> {
    Optional<CryptoRate> findCryptoRateByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);

}
