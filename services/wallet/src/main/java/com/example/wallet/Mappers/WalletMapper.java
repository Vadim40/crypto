package com.example.wallet.Mappers;

import com.example.wallet.Models.DTO.TokenDTO;
import com.example.wallet.Models.DTO.WalletDTO;
import com.example.wallet.Models.Token;
import com.example.wallet.Models.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WalletMapper {

    private final TokenMapper tokenMapper;


    public WalletDTO mapWalletToWalletDTO(Wallet wallet) {
        Set<TokenDTO> tokenDTOs = wallet.getTokens().stream()
                .map(tokenMapper::mapTokenToTokenDTO)
                .collect(Collectors.toSet());

        return new WalletDTO(
                wallet.getId(),
                wallet.getAccountId(),
                wallet.getAddress(),
                tokenDTOs
        );
    }


}
