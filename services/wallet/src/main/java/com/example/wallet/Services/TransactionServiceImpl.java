package com.example.wallet.Services;

import com.example.wallet.Exceptions.TransactionNotFoundException;
import com.example.wallet.Models.DTO.AccountResponse;
import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Repositories.TransactionRepository;
import com.example.wallet.Services.Interfaces.AccountService;
import com.example.wallet.Services.Interfaces.TokenService;
import com.example.wallet.Services.Interfaces.TransactionService;
import com.example.wallet.Services.Interfaces.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TokenService tokenService;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final AccountService accountService;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction, Long id) {
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by this id: "+id));
    }


    @Override
    public List<Transaction> findTransactionsByType(TransactionType transactionType) {
        Long accountId = accountService.getCurrentAccount().id();
        return transactionRepository.findTransactionsByTransactionTypeAndAccountId(transactionType, accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found for type: " + transactionType));
    }

    @Override
    public List<Transaction> findTransactionsAfterDate(LocalDate localDate) {
        Long accountId = accountService.getCurrentAccount().id();
        return transactionRepository.findTransactionsByTransactionDateAfterAndAccountId(localDate, accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found after: " + localDate));
    }

    @Override
    public List<Transaction> findAllTransactions() {
        Long accountId = accountService.getCurrentAccount().id();
        return transactionRepository.findTransactionsByAccountId(accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found"));
    }

    @Override
    @Transactional
    public void transferTokens(String destinationAddress, String symbol, BigDecimal amount) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenTransfer(accountResponse, destinationAddress, symbol, amount);
    }


    @Transactional
    @Override
    public void depositTokens(String symbol, BigDecimal amount) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenDeposit(accountResponse, symbol, amount);
    }


    @Transactional
    @Override
    public void withdrawTokens(String symbol, BigDecimal amount) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenWithdrawal(accountResponse, symbol, amount);
    }


    @Transactional
    @Override
    public void receiveTokens(String sourceAddress, String symbol, BigDecimal amount) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenReceive(accountResponse,sourceAddress, symbol, amount );
    }

    private void processTokenTransfer(AccountResponse accountResponse, String destinationAddress, String symbol, BigDecimal amount) {
        Wallet sourceWallet = walletService.findWalletByAccountId(accountResponse.id());
        Wallet destinationWallet = walletService.findWalletByAddress(destinationAddress);

        tokenService.transferTokens(symbol, amount, sourceWallet, destinationWallet);

        createAndSaveTransaction(accountResponse.id(), sourceWallet, destinationWallet, symbol, amount, TransactionType.TRANSFER);
    }

    private void processTokenDeposit(AccountResponse accountResponse, String symbol, BigDecimal amount) {
        Wallet wallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.addTokens(symbol, amount, wallet);

        createAndSaveTransaction(accountResponse.id(), null, wallet, symbol, amount, TransactionType.DEPOSIT);
    }

    private void processTokenWithdrawal(AccountResponse accountResponse, String symbol, BigDecimal amount) {
        Wallet wallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.subtractTokens(symbol, amount, wallet);

        createAndSaveTransaction(accountResponse.id(), wallet, null, symbol, amount, TransactionType.WITHDRAWAL);
    }

    private void processTokenReceive(AccountResponse accountResponse, String sourceAddress, String symbol, BigDecimal amount) {
        Wallet sourceWallet = walletService.findWalletByAddress(sourceAddress);
        Wallet destinationWallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.transferTokens(symbol, amount, sourceWallet, destinationWallet);

        createAndSaveTransaction(accountResponse.id(), sourceWallet, destinationWallet, symbol, amount, TransactionType.RECEIVE);
    }


    private void createAndSaveTransaction(Long accountId, Wallet sourceWallet, Wallet destinationWallet, String symbol, BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setSourceWallet(sourceWallet);
        transaction.setDestinationWallet(destinationWallet);
        transaction.setTokenType(symbol);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(transactionType);
        transactionRepository.save(transaction);
    }

}
