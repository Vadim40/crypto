package com.example.exchange.Mappers;

import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Models.DTOs.CryptoRateResponse;
import org.springframework.stereotype.Component;

@Component
public class CryptoRateMapper {

    public CryptoRate mapCryptoRateResponseToCryptoRate(CryptoRateResponse cryptoRateResponse) {
        CryptoRate cryptoRate = new CryptoRate();
        cryptoRate.setSymbol(cryptoRateResponse.getSymbol());
        cryptoRate.setRate(cryptoRateResponse.getPriceInUSDT());
        return cryptoRate;
    }
}
