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
@Table(name = "transfer_transactions")
public class TransferTransaction extends Transaction {

    @ManyToOne
    @JoinColumn(name = "source_wallet_id", nullable = false)
    private Wallet sourceWallet;

    @ManyToOne
    @JoinColumn(name = "destination_wallet_id", nullable = false)
    private Wallet destinationWallet;

    @Column(name = "token_symbol", nullable = false)
    private String tokenSymbol;

    @Column(name = "amount", nullable = false, precision = 19, scale = 8)
    private BigDecimal amount;
}
