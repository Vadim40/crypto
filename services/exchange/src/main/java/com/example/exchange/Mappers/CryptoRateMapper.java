package com.example.exchange.Mappers;

import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Models.DTOs.CryptoRateDTO;
import com.example.exchange.Models.DTOs.CryptoRateResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Component
public class CryptoRateMapper {

    public List<CryptoRate> mapCryptoRateResponseToCryptoRates(CryptoRateResponse cryptoRateResponse) {
        String baseCurrency = cryptoRateResponse.getSymbol();
        return cryptoRateResponse.getQuote().entrySet().stream()
                .flatMap(entry -> {
                    String targetCurrency = entry.getKey();
                    BigDecimal rate = entry.getValue().getPrice();


                    CryptoRate directRate = new CryptoRate();
                    directRate.setBaseCurrency(baseCurrency);
                    directRate.setTargetCurrency(targetCurrency);
                    directRate.setRate(rate);

                    CryptoRate inverseRate = new CryptoRate();
                    inverseRate.setBaseCurrency(targetCurrency);
                    inverseRate.setTargetCurrency(baseCurrency);
                    inverseRate.setRate(BigDecimal.ONE.divide(rate, MathContext.DECIMAL128));

                    return Stream.of(directRate, inverseRate);
                })
                .collect(Collectors.toList());
    }
    public CryptoRateDTO mapCryptoRateToCryptoRateDTO(CryptoRate cryptoRate){
        return new CryptoRateDTO(
                cryptoRate.getBaseCurrency(),
                cryptoRate.getTargetCurrency(),
                cryptoRate.getRate());

    }
}
