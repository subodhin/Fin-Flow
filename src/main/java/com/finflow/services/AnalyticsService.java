package com.finflow.services;


import com.finflow.dto.trasactions.PlatformSummaryDTO;
import com.finflow.dto.users.ExpenseBreakdownDTO;
import com.finflow.dto.users.TransactionAnalyticsDTO;
import com.finflow.dto.users.UserMonthlySummaryDTO;
import com.finflow.entity.Transaction;
import com.finflow.enums.TransactionType;
import com.finflow.repository.TransactionRepository;
import com.finflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public UserMonthlySummaryDTO getMonthlySummary(Long userId) {
                List<Transaction> transactions =
                transactionRepository.findByUserId(userId);
        BigDecimal totalIncome =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getType() == TransactionType.INCOME)
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getType() == TransactionType.EXPENSE)
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        return UserMonthlySummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    public TransactionAnalyticsDTO getHighestExpense(Long userId) {

        List<Transaction> transactions =
                transactionRepository.findByUserId(userId);


              Optional<Transaction> highestExpense =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getType() == TransactionType.EXPENSE)
                        .max(
                                Comparator.comparing(
                                        Transaction::getAmount
                                )
                        );

        return highestExpense.map(transaction -> TransactionAnalyticsDTO.builder()
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getCreatedAt())
                .build()).orElse(null);
    }


    public TransactionAnalyticsDTO getHighestIncome(Long userId) {

        List<Transaction> transactions =
                transactionRepository.findByUserId(userId);

        Optional<Transaction> highestIncome =
                transactions.stream()
                        .filter(transaction ->
                                transaction.getType() == TransactionType.INCOME)
                        .max(
                                Comparator.comparing(
                                        Transaction::getAmount
                                )
                        );

        return highestIncome.map(transaction -> TransactionAnalyticsDTO.builder()
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getCreatedAt())
                .build()).orElse(null);
    }


    public List<ExpenseBreakdownDTO> getExpenseBreakdown(Long userId) {

        List<Transaction> expenses =
                transactionRepository.findByUserId(userId)
                        .stream()
                        .filter(transaction ->
                                transaction.getType() == TransactionType.EXPENSE)
                        .toList();

        Map<String, BigDecimal> expenseMap =
                expenses.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Transaction::getDescription,
                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                Transaction::getAmount,
                                                BigDecimal::add
                                        )
                                )
                        );

        return expenseMap.entrySet()
                .stream()
                .map(entry ->
                        ExpenseBreakdownDTO.builder()
                                .description(entry.getKey())
                                .totalAmount(entry.getValue())
                                .build()
                )
                .toList();
    }

        public PlatformSummaryDTO getPlatformSummary() {

            Long totalUsers = userRepository.count();

            List<Transaction> transactions =
                    transactionRepository.findAll();

            BigDecimal totalIncome =
                    transactions.stream()
                            .filter(transaction ->
                                    transaction.getType() == TransactionType.INCOME)
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpense =
                    transactions.stream()
                            .filter(transaction ->
                                    transaction.getType() == TransactionType.EXPENSE)
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal netBalance =
                    totalIncome.subtract(totalExpense);

            return PlatformSummaryDTO.builder()
                    .totalUsers(totalUsers)
                    .totalIncome(totalIncome)
                    .totalExpense(totalExpense)
                    .netBalance(netBalance)
                    .build();
        }


}
