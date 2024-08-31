package com.example.exchange.Controllers;

import com.example.exchange.Mappers.CryptoRateMapper;
import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Models.DTOs.ConvertRequest;
import com.example.exchange.Models.DTOs.CryptoRateDTO;
import com.example.exchange.Models.DTOs.ExchangeRequest;
import com.example.exchange.Services.Interfaces.CryptoRateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crypto-rates")
public class CryptoRateController {
    private final CryptoRateService cryptoRateService;
    private final CryptoRateMapper cryptoRateMapper;
    @GetMapping("/exchange")
    ResponseEntity<CryptoRateDTO> findExchangeRate(@Valid @RequestBody ExchangeRequest request){
        CryptoRate cryptoRate=cryptoRateService.findCryptoRate(request.baseCurrency(), request.targetCurrency());
        CryptoRateDTO cryptoRateDTO=cryptoRateMapper.mapCryptoRateToCryptoRateDTO(cryptoRate);
        return new ResponseEntity<>(cryptoRateDTO, HttpStatus.OK);
    }
    @GetMapping("/convert")
    ResponseEntity<Object> convertCurrencies(@Valid @RequestBody ConvertRequest request){
        cryptoRateService.executeCurrencyConversion(request.baseCurrency(),request.targetCurrency(), request.amount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
