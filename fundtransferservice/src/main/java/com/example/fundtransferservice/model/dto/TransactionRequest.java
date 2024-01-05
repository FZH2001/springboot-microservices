package com.example.fundtransferservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private String donorId;
    private String beneficiaryId;
    private String transactionReference;
    private BigDecimal amount;
    private String whoPayFees;

    private boolean isNotificationFees;
    private boolean notify;
    private double fraisTransfert;
    private String authID;
    private String RefundReason;
}
