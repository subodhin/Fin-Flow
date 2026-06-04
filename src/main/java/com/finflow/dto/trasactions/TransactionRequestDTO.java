package com.finflow.dto.trasactions;

import com.finflow.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {

    private BigDecimal amount;

    private TransactionType type;

    private String description;

    private Long userId;
}
