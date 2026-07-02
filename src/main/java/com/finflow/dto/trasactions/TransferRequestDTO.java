package com.finflow.dto.trasactions;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDTO {

    private Long fromUserId;

    private Long toUserId;

    private BigDecimal amount;
}