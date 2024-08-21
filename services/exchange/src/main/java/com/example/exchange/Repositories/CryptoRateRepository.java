package com.example.exchange.Repositories;

import com.example.exchange.Models.CryptoRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoRateRepository extends JpaRepository<CryptoRate, Long> {
    CryptoRate findCryptoRateBySymbol(String symbol);

}
