package com.finflow.services;

import com.finflow.analytics.DuplicateTransactionKey;
import com.finflow.dto.trasactions.DuplicateTransactionDTO;
import com.finflow.dto.trasactions.TransactionRequestDTO;
import com.finflow.dto.trasactions.TransactionResponseDTO;
import com.finflow.dto.trasactions.UserFinancialSummaryDTO;
import com.finflow.entity.Transaction;
import com.finflow.entity.User;
import com.finflow.enums.TransactionType;
import com.finflow.repository.TransactionRepository;
import com.finflow.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                .orElseThrow();

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
                .orElseThrow();

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
                .orElseThrow();

        User user = userRepository.findById(request.getUserId())
                .orElseThrow();

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
                .orElseThrow();

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
}
