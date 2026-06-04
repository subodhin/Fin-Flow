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
public class DuplicateTransactionDTO {

    private Long userId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private Long duplicateCount;
}