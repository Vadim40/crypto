package com.example.exchange.Services;

import com.example.exchange.Mappers.CryptoRateMapper;
import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Models.DTOs.CryptoRateResponse;
import com.example.exchange.Services.Interfaces.CryptoRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoRateScheduler {
    private final CoinmarketService coinmarketService;
    private final CryptoRateService cryptoRateService;
    private final CryptoRateMapper cryptoRateMapper;

    @Scheduled(initialDelay = 0, fixedRate = 3600000)
    public  void updateCryptoRates(){
        String targetCurrency = "USDT";
        List<CryptoRateResponse> rateResponses = coinmarketService.getCryptoRates(targetCurrency);

        for (CryptoRateResponse rateResponse : rateResponses) {
            List<CryptoRate> rates = cryptoRateMapper.mapCryptoRateResponseToCryptoRates(rateResponse);
            for (CryptoRate rate : rates) {
                cryptoRateService.updateCryptoRate(rate);
            }
        }
    }

}
