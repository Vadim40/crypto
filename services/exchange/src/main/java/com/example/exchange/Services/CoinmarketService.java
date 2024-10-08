package com.example.exchange.Services;

import com.example.exchange.Models.DTOs.CryptoRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CoinmarketService {

    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<CryptoRateResponse> getCryptoRates(String targetCurrency) {
        String url = String.format(
                "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=%s",
                targetCurrency
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );


        Map<String, Object> responseBody = responseEntity.getBody();
        List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");

        List<CryptoRateResponse> cryptoRates = new ArrayList<>();
        for (Map<String, Object> item : data) {
            String symbol = (String) item.get("symbol");

            Map<String, Object> quote = (Map<String, Object>) item.get("quote");
            Map<String, Object> targetCurrencyData = (Map<String, Object>) quote.get(targetCurrency);
            BigDecimal price = new BigDecimal(targetCurrencyData.get("price").toString());

            CryptoRateResponse rateResponse = new CryptoRateResponse();
            rateResponse.setSymbol(symbol);
            rateResponse.setQuote(Map.of(targetCurrency, new CryptoRateResponse.CurrencyQuote(price)));

            cryptoRates.add(rateResponse);
        }

        return cryptoRates;
    }
}
