package com.example.wallet.Services;

import com.example.wallet.Exceptions.TransactionNotFoundException;
import com.example.wallet.Models.*;
import com.example.wallet.Models.DTO.AccountResponse;
import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Repositories.*;
import com.example.wallet.Services.Interfaces.AccountService;
import com.example.wallet.Services.Interfaces.TokenService;
import com.example.wallet.Services.Interfaces.TransactionService;
import com.example.wallet.Services.Interfaces.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TokenService tokenService;
    private final WalletService walletService;
    private final AccountService accountService;

    private final TransactionRepository transactionRepository;


    @Override
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by id: " + id));
    }

    @Override
    public List<Transaction> findAccountTransactionsByTransactionType(TransactionType transactionType) {
        Long accountId = accountService.getCurrentAccount().id();
        return transactionRepository.findTransactionsByTransactionTypeAndAccountId(transactionType, accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found for type: " + transactionType));
    }
    @Override
    public List<Transaction> findAccountTransactionsByTransactionTypeAfterDate(TransactionType transactionType, LocalDate date) {
        Long accountId = accountService.getCurrentAccount().id();

        return transactionRepository.findTransactionsByTransactionTypeAndTransactionDateAfterAndAccountId(transactionType, date, accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found for type: " + transactionType + ", after date: " + date ));
    }



    @Transactional
    @Override
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
        processTokenReceive(accountResponse, sourceAddress, symbol, amount);
    }

    private void processTokenTransfer(AccountResponse accountResponse, String destinationAddress, String symbol, BigDecimal amount) {
        Wallet sourceWallet = walletService.findWalletByAccountId(accountResponse.id());
        Wallet destinationWallet = walletService.findWalletByAddress(destinationAddress);

        tokenService.transferTokens(symbol, amount, sourceWallet, destinationWallet);

        createAndSaveTransferTransaction(accountResponse.id(), sourceWallet, destinationWallet, symbol, amount);
    }

    private void processTokenDeposit(AccountResponse accountResponse, String symbol, BigDecimal amount) {
        Wallet wallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.addTokens(symbol, amount, wallet);

        createAndSaveDepositTransaction(accountResponse.id(), wallet, symbol, amount);
    }

    private void processTokenWithdrawal(AccountResponse accountResponse, String symbol, BigDecimal amount) {
        Wallet wallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.subtractTokens(symbol, amount, wallet);

        createAndSaveWithdrawalTransaction(accountResponse.id(), wallet, symbol, amount);
    }

    private void processTokenReceive(AccountResponse accountResponse, String sourceAddress, String symbol, BigDecimal amount) {
        Wallet sourceWallet = walletService.findWalletByAddress(sourceAddress);
        Wallet destinationWallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.transferTokens(symbol, amount, sourceWallet, destinationWallet);

        createAndSaveReceiveTransaction(accountResponse.id(), sourceWallet, destinationWallet, symbol, amount);
    }

    private void createAndSaveTransferTransaction(Long accountId, Wallet sourceWallet, Wallet destinationWallet, String symbol, BigDecimal amount) {
        TransferTransaction transaction = new TransferTransaction();
        transaction.setAccountId(accountId);
        transaction.setSourceWallet(sourceWallet);
        transaction.setDestinationWallet(destinationWallet);
        transaction.setTokenSymbol(symbol);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.TRANSFER);

        transactionRepository.save(transaction);
    }

    private void createAndSaveDepositTransaction(Long accountId, Wallet wallet, String symbol, BigDecimal amount) {
        DepositTransaction transaction = new DepositTransaction();
        transaction.setAccountId(accountId);
        transaction.setWallet(wallet);
        transaction.setTokenSymbol(symbol);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);

        transactionRepository.save(transaction);
    }

    private void createAndSaveWithdrawalTransaction(Long accountId, Wallet wallet, String symbol, BigDecimal amount) {
        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setAccountId(accountId);
        transaction.setWallet(wallet);
        transaction.setTokenSymbol(symbol);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);

        transactionRepository.save(transaction);
    }

    private void createAndSaveReceiveTransaction(Long accountId, Wallet sourceWallet, Wallet destinationWallet, String symbol, BigDecimal amount) {
        ReceiveTransaction transaction = new ReceiveTransaction();
        transaction.setAccountId(accountId);
        transaction.setSourceWallet(sourceWallet);
        transaction.setDestinationWallet(destinationWallet);
        transaction.setTokenSymbol(symbol);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.RECEIVE);

        transactionRepository.save(transaction);
    }


}
