package com.finflow.services;

import com.finflow.analytics.DuplicateTransactionKey;
import com.finflow.dto.trasactions.*;
import com.finflow.entity.Transaction;
import com.finflow.entity.User;
import com.finflow.enums.TransactionType;
import com.finflow.exception.ResourceNotFoundException;
import com.finflow.repository.TransactionRepository;
import com.finflow.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionResponseDTO createTransaction (TransactionRequestDTO request){

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + request.getUserId()));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .user(user)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return TransactionResponseDTO.builder()
                .id(saved.getId())
                .amount(saved.getAmount())
                .type(saved.getType())
                .description(saved.getDescription())
                .userId(saved.getUser().getId())
                .build();
    }

    public TransactionResponseDTO getTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found with id " + id));

        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .userId(transaction.getUser().getId())
                .build();
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> TransactionResponseDTO.builder()
                        .id(transaction.getId())
                        .amount(transaction.getAmount())
                        .type(transaction.getType())
                        .description(transaction.getDescription())
                        .userId(transaction.getUser().getId())
                        .build())
                .toList();
    }

    public TransactionResponseDTO updateTransaction(
            Long id,
            TransactionRequestDTO request) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "transaction not found with id " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id));

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setUser(user);

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return TransactionResponseDTO.builder()
                .id(updatedTransaction.getId())
                .amount(updatedTransaction.getAmount())
                .type(updatedTransaction.getType())
                .description(updatedTransaction.getDescription())
                .userId(updatedTransaction.getUser().getId())
                .build();
    }

    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "transaction not found with id " + id));

        transactionRepository.delete(transaction);
    }

    public UserFinancialSummaryDTO getFinancialSummary(Long userId) {
        List<Transaction> transactions =
                transactionRepository.findByUserId(userId);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal currentBalance =
                totalIncome.subtract(totalExpense);

        long totalTransactions = transactions.size();

        return UserFinancialSummaryDTO.builder()
                .userId(userId)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .currentBalance(currentBalance)
                .totalTransactions(totalTransactions)
                .build();

    }

    public List<DuplicateTransactionDTO> getDuplicateTransactions() {

        List<Transaction> transactions =
                transactionRepository.findAll();

        Map<DuplicateTransactionKey, Long> duplicates =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                transaction -> new DuplicateTransactionKey(
                                        transaction.getUser().getId(),
                                        transaction.getAmount(),
                                        transaction.getType(),
                                        transaction.getDescription()
                                ),
                                Collectors.counting()
                        ));

        return duplicates.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> DuplicateTransactionDTO.builder()
                        .userId(entry.getKey().userId())
                        .amount(entry.getKey().amount())
                        .type(entry.getKey().type())
                        .description(entry.getKey().description())
                        .duplicateCount(entry.getValue())
                        .build())
                .toList();
    }

//    public List<TransactionResponseDTO> getTransactions(TransactionType type, String description, BigDecimal minAmount, BigDecimal maxAmount, String sortBy) {
//        List<Transaction> transactions = transactionRepository.findAll();
//        if (type != null) {
//            transactions = transactions.stream().filter(transaction -> transaction.getType() == type).toList();
//        }
//        if (description != null && !description.isBlank()) {
//            transactions = transactions.stream()
//                    .filter(transaction ->
//                            transaction.getDescription() != null &&
//                                    transaction.getDescription()
//                                            .toLowerCase()
//                                            .contains(description.toLowerCase()))
//                    .toList();
//        }
//        if (minAmount != null) {
//            transactions = transactions.stream()
//                    .filter(transaction ->
//                            transaction.getAmount()
//                                    .compareTo(minAmount) >= 0)
//                    .toList();
//        }
//        if (maxAmount != null) {
//            transactions = transactions.stream()
//                    .filter(transaction ->
//                            transaction.getAmount()
//                                    .compareTo(maxAmount) <= 0)
//                    .toList();
//        }
//
//        if (minAmount != null
//                && maxAmount != null
//                && minAmount.compareTo(maxAmount) > 0) {
//
//            throw new IllegalArgumentException(
//                    "minAmount cannot be greater than maxAmount");
//        }
//
//        if ("amount".equalsIgnoreCase(sortBy)) {
//            transactions = transactions.stream()
//                    .sorted(
//                            Comparator.comparing(
//                                    Transaction::getAmount
//                            )
//                    )
//                    .toList();
//        }
//
//        if ("date".equalsIgnoreCase(sortBy)) {
//            transactions = transactions.stream()
//                    .sorted(
//                            Comparator.comparing(
//                                    Transaction::getCreatedAt
//                            )
//                    )
//                    .toList();
//        }
//
//        if ("dateDesc".equalsIgnoreCase(sortBy)) {
//            transactions = transactions.stream()
//                    .sorted(
//                            Comparator.comparing(
//                                    Transaction::getCreatedAt
//                            ).reversed()
//                    )
//                    .toList();
//        }
//
//        return transactions.stream()
//                .map(transaction -> TransactionResponseDTO.builder()
//                        .id(transaction.getId())
//                        .amount(transaction.getAmount())
//                        .type(transaction.getType())
//                        .description(transaction.getDescription())
//                        .userId(transaction.getUser().getId())
//                        .build())
//                .toList();
//    }

    public Page<TransactionResponseDTO> getTransactions(
            TransactionType type,
            String description,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            String sortBy,
            int page,
            int size) {

        List<Transaction> transactions = transactionRepository.findAll();

        if (type != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getType() == type)
                    .toList();
        }

        if (description != null && !description.isBlank()) {
            transactions = transactions.stream()
                    .filter(transaction ->
                            transaction.getDescription() != null &&
                                    transaction.getDescription()
                                            .toLowerCase()
                                            .contains(description.toLowerCase()))
                    .toList();
        }

        if (minAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction ->
                            transaction.getAmount()
                                    .compareTo(minAmount) >= 0)
                    .toList();
        }

        if (maxAmount != null) {
            transactions = transactions.stream()
                    .filter(transaction ->
                            transaction.getAmount()
                                    .compareTo(maxAmount) <= 0)
                    .toList();
        }

        if (minAmount != null
                && maxAmount != null
                && minAmount.compareTo(maxAmount) > 0) {

            throw new IllegalArgumentException(
                    "minAmount cannot be greater than maxAmount");
        }

        if ("amount".equalsIgnoreCase(sortBy)) {
            transactions = transactions.stream()
                    .sorted(Comparator.comparing(Transaction::getAmount))
                    .toList();
        }

        if ("date".equalsIgnoreCase(sortBy)) {
            transactions = transactions.stream()
                    .sorted(Comparator.comparing(Transaction::getCreatedAt))
                    .toList();
        }

        if ("dateDesc".equalsIgnoreCase(sortBy)) {
            transactions = transactions.stream()
                    .sorted(
                            Comparator.comparing(
                                    Transaction::getCreatedAt
                            ).reversed()
                    )
                    .toList();
        }

        List<TransactionResponseDTO> dtoList =
                transactions.stream()
                        .map(transaction ->
                                TransactionResponseDTO.builder()
                                        .id(transaction.getId())
                                        .amount(transaction.getAmount())
                                        .type(transaction.getType())
                                        .description(transaction.getDescription())
                                        .userId(transaction.getUser().getId())
                                        .build())
                        .toList();

        int start = page * size;
        int end = Math.min(start + size, dtoList.size());

        List<TransactionResponseDTO> pagedList =
                dtoList.subList(start, end);

        return new PageImpl<>(
                pagedList,
                PageRequest.of(page, size),
                dtoList.size()
        );
    }

    public List<TransactionTypeSummaryDTO> getTransactionTypeSummary() {

        Map<TransactionType, List<Transaction>> groupedTransactions =
                transactionRepository.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Transaction::getType
                        ));

        return groupedTransactions.entrySet()
                .stream()
                .map(entry -> {

                    BigDecimal totalAmount = entry.getValue()
                            .stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return TransactionTypeSummaryDTO.builder()
                            .type(entry.getKey())
                            .transactionCount(
                                    (long) entry.getValue().size()
                            )
                            .totalAmount(totalAmount)
                            .build();
                })
                .toList();
    }

//
//    public Page<TransactionResponseDTO> getTransactions(
//            int page,
//            int size) {
//
//        Pageable pageable =
//                PageRequest.of(page, size);
//
//        Page<Transaction> transactions =
//                transactionRepository.findAll(pageable);
//
//        return transactions.map(transaction ->
//                TransactionResponseDTO.builder()
//                        .id(transaction.getId())
//                        .amount(transaction.getAmount())
//                        .type(transaction.getType())
//                        .description(transaction.getDescription())
//                        .userId(transaction.getUser().getId())
//                        .build()
//        );
//    }
}
