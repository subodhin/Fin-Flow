package com.finflow.dto.trasactions;

import com.finflow.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTypeSummaryDTO {

    private TransactionType type;

    private Long transactionCount;

    private BigDecimal totalAmount;
}