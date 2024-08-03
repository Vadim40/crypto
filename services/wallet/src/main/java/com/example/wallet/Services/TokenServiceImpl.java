package com.example.wallet.Services;

import com.example.wallet.Exceptions.TokenNotFoundException;
import com.example.wallet.Models.Token;
import com.example.wallet.Repositories.TokenRepository;
import com.example.wallet.Services.Interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
private final TokenRepository tokenRepository;

        @Override
        public Token saveToken(Token token) {
            return tokenRepository.save(token);
        }

        @Override
        public Token updateToken(Token token, Long id) {
            token.setId(id);
            return tokenRepository.save(token);
        }

        @Override
        public void deleteToken(Long id) {
            tokenRepository.deleteById(id);

        }

        @Override
        public Token findTokenById(Long id) {
            return tokenRepository.findById(id).orElseThrow(()->new TokenNotFoundException("token not found"));
        }
}
