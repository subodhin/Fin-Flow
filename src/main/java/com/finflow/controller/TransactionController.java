package com.finflow.controller;

import com.finflow.dto.trasactions.DuplicateTransactionDTO;
import com.finflow.dto.trasactions.TransactionRequestDTO;
import com.finflow.dto.trasactions.TransactionResponseDTO;
import com.finflow.dto.trasactions.TransactionTypeSummaryDTO;
import com.finflow.entity.Transaction;
import com.finflow.enums.TransactionType;
import com.finflow.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionResponseDTO createTransaction(@Valid @RequestBody TransactionRequestDTO request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(
            @PathVariable Long id) {

        return transactionService.getTransactionById(id);
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(
           @PathVariable Long id,
           @Valid  @RequestBody TransactionRequestDTO request) {

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

//    @GetMapping
//    public List<TransactionResponseDTO> getTransactions(
//            @RequestParam(required = false) TransactionType type,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) BigDecimal minAmount,
//            @RequestParam(required = false) BigDecimal maxAmount,
//            @RequestParam(required = false) String sortBy) {
//
//        return transactionService.getTransactions(
//                type,
//                description,
//                minAmount,
//                maxAmount,
//                sortBy);
//    }

//    @GetMapping temp
//    public Page<TransactionResponseDTO> getTransactions(
//            @RequestParam(required = false) TransactionType type,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) BigDecimal minAmount,
//            @RequestParam(required = false) BigDecimal maxAmount,
//            @RequestParam(required = false) String sortBy,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return transactionService.getTransactions(
//                type,
//                description,
//                minAmount,
//                maxAmount,
//                sortBy,
//                page,
//                size);
//    }

    @GetMapping("/paged")
    public Page<TransactionResponseDTO> getTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return transactionService.getTransactions(type, userId,
                page,
                size);
    }

    @GetMapping("/group-by-type")
    public List<TransactionTypeSummaryDTO> getTransactionTypeSummary() {

        return transactionService.getTransactionTypeSummary();
    }

    @GetMapping("/testDerivedQuery")
    public Page<TransactionResponseDTO> getTransactionsAboveAmount(
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return transactionService.getTransactionsAboveAmount(amount, page, size);
    }

    @GetMapping("/native")
    public Page<TransactionResponseDTO> getTransactionsAboveAmountNative(
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return transactionService
                .getTransactionsAboveAmountNative(
                        amount,
                        page,
                        size);
    }




}
