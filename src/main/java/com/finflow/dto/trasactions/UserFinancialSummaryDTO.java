package com.finflow.dto.trasactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFinancialSummaryDTO {

    private Long userId;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal currentBalance;

    private Long totalTransactions;
}
