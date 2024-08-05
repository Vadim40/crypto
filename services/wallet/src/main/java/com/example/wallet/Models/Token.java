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
                @Index(name = "idx_wallet_token_type", columnList = "wallet_id, token_type")
        })
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
}