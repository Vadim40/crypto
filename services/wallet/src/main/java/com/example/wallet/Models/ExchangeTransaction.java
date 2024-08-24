package com.example.wallet.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchange_transactions")
public class ExchangeTransaction extends Transaction {
    @ManyToOne
    @JoinColumn(name = "source_wallet_id", nullable = false)
    private Wallet Wallet;

    @Column(name = "token_symbol_from", nullable = false)
    private String tokenSymbolFrom;
    @Column(name = "token_symbol_to", nullable = false)
    private String tokenSymbolTo;

    @Column(name = "amount_from", nullable = false, precision = 19, scale = 8)
    private BigDecimal amountFrom;

    @Column(name = "amount_from", nullable = false, precision = 19, scale = 8)
    private BigDecimal amountTo;
}
