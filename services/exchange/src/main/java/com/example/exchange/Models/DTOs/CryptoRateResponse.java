package com.example.exchange.Models.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CryptoRateResponse {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("quote")
    private Map<String, CurrencyQuote> quote;

    @Data
    @AllArgsConstructor
    public static class CurrencyQuote {
        @JsonProperty("price")
        private BigDecimal price;
    }
}
