package com.example.exchange.Services;

import com.example.exchange.Models.DTOs.CryptoRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@Service
public class CoinmarketService {

    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate=new RestTemplate();


    public List<CryptoRateResponse> getCryptoRates(String targetCurrency) {
        String url = String.format(
                "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=%s",
                targetCurrency
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<CryptoRateResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return responseEntity.getBody();
    }
}
