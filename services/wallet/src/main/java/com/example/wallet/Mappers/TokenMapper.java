package com.example.wallet.Mappers;

import com.example.wallet.Models.DTO.TokenDTO;
import com.example.wallet.Models.Token;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {


    public TokenDTO mapTokenToTokenDTO(Token token) {
        return new TokenDTO(
                token.getId(),
                token.getSymbol(),
                token.getAmount()
        );
    }



}
