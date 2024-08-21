package com.example.exchange.Services;

import com.example.exchange.Models.DTOs.CryptoRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CoinmarketService {

    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate =new RestTemplate();


    public List<CryptoRateResponse> getCryptoRatesInUSDT() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?convert=USDT";

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
