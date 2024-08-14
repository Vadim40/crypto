package com.example.wallet.Controllers;

import com.example.wallet.Mappers.TransactionMapper;
import com.example.wallet.Models.DTO.*;
import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;
import com.example.wallet.Services.Interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable("id") Long id) {
        Transaction transaction = transactionService.findTransactionById(id);
        TransactionDTO transactionDTO = transactionMapper.mapTransactionToTransactionDTO(transaction);
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransactionDTO>> findAllTransactions() {
        List<Transaction> transactions = transactionService.findAllTransactions();
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::mapTransactionToTransactionDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<TransactionDTO>> findTransactionsByType(@RequestParam TransactionType transactionType) {
        List<Transaction> transactions = transactionService.findTransactionsByType(transactionType);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::mapTransactionToTransactionDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<TransactionDTO>> findTransactionsAfterDate(@RequestParam LocalDate date) {
        List<Transaction> transactions = transactionService.findTransactionsAfterDate(date);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::mapTransactionToTransactionDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }

    @PostMapping("/transfer-tokens")
    public ResponseEntity<Object> transferTokens(@RequestBody @Valid TransferRequest request) {
        transactionService.transferTokens(request.destinationAddress(), request.tokenType(), request.amount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/deposit-tokens")
    public ResponseEntity<Object> depositTokens(@RequestBody @Valid DepositRequest request) {
        transactionService.depositTokens(request.tokenType(), request.amount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/withdrawal-tokens")
    public ResponseEntity<Object> withdrawalTokens(@RequestBody @Valid WithdrawalRequest request) {
        transactionService.withdrawTokens(request.tokenType(), request.amount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/receive-tokens")
    public ResponseEntity<Object> receiveTokens(@RequestBody @Valid ReceiveRequest request) {
        transactionService.receiveTokens(request.sourceAddress(), request.tokenType(), request.amount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
