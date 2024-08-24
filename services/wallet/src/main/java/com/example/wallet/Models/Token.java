package com.example.wallet.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "token",
        indexes = {
                @Index(name = "idx_wallet_token_type", columnList = "wallet_id, symbol")
        })
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "amount",  nullable = false, precision = 19, scale = 8)
    private BigDecimal amount;
}