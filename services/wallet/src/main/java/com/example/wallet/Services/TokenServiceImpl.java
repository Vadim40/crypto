package com.example.wallet.Services;

import com.example.wallet.Exceptions.NotEnoughTokenAmountException;
import com.example.wallet.Exceptions.TokenNotFoundException;
import com.example.wallet.Models.Token;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Repositories.TokenRepository;
import com.example.wallet.Services.Interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
        return tokenRepository.findById(id).orElseThrow(() -> new TokenNotFoundException("token not found"));
    }

    @Override
    public Token findTokenByTokenTypeAndWallet(String type, Wallet wallet) {
        return tokenRepository.findTokenByTokenTypeAndWallet(type, wallet).orElseThrow(() -> new TokenNotFoundException("token not found"));
    }

    @Override
    @Transactional
    public void TransferTokens(String type, BigDecimal amount, Wallet sourceWallet, Wallet destinationWallet) {
        Token sourceToken = findTokenByTokenTypeAndWallet(type, sourceWallet);
        Token destinationToken = findTokenByTokenTypeAndWallet(type, destinationWallet);
        decreaseAmount(sourceToken, amount);
        increaseAmount(destinationToken, amount);
        saveToken(sourceToken);
        saveToken(destinationToken);
    }

    private void increaseAmount(Token token, BigDecimal amount) {
        BigDecimal balance = token.getAmount().add(amount);
        token.setAmount(balance);
    }

    private void decreaseAmount(Token token, BigDecimal amount) {
        BigDecimal balance = token.getAmount().subtract(amount);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughTokenAmountException("not enough amount of token");
        }
        token.setAmount(balance);
    }
}
