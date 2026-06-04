package com.finflow.analytics;

import com.finflow.enums.TransactionType;

import java.math.BigDecimal;

public record DuplicateTransactionKey(Long userId,
                                      BigDecimal amount,
                                      TransactionType type,
                                      String description) {
}
