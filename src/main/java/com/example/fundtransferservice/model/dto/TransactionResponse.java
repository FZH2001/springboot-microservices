package com.example.fundtransferservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransactionResponse {
    private String message;
    private String transactionId;
}
