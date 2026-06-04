package com.finflow.controller;

import com.finflow.dto.trasactions.DuplicateTransactionDTO;
import com.finflow.dto.trasactions.TransactionRequestDTO;
import com.finflow.dto.trasactions.TransactionResponseDTO;
import com.finflow.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponseDTO createTransaction(@RequestBody TransactionRequestDTO request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(
            @PathVariable Long id) {

        return transactionService.getTransactionById(id);
    }

    @GetMapping
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO request) {

        return transactionService.updateTransaction(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }


    @GetMapping("/duplicates")
    public List<DuplicateTransactionDTO> getDuplicates() {
        return transactionService.getDuplicateTransactions();
    }



}
