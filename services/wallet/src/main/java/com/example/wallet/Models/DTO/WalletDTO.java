package com.example.wallet.Models.DTO;

import java.util.Set;

public record WalletDTO(
        Long id,
        Long accountId,
        String address,
        Set<TokenDTO> tokens
) {}