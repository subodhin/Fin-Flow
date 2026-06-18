package com.finflow.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAnalyticsDTO {

    private Long transactionId;

    private BigDecimal amount;

    private String description;

    private LocalDateTime date;


}
