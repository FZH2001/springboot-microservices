package com.example.fundtransferservice.model.dto;

import com.example.fundtransferservice.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private Long donorId;
    private Long agentId;
    private Long beneficiaryId;
    private String transactionReference;
    private double amount;
    private String whoPayFees;

    private boolean isNotificationFees;
    private boolean notify;
    private double fraisTransfert;
    private String authID;
    private String RefundReason;
    private TransactionType paymentType;
}
