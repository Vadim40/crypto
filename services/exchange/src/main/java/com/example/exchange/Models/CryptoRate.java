package com.example.exchange.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class CryptoRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String baseCurrency;

    @Column(nullable = false)
    private String targetCurrency;

    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal rate;

}
