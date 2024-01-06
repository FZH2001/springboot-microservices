package com.example.fundtransferservice.model.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    private Long donorId;
    private Long beneficiaryId;
    private Long agentId;
    private double amount;
    private String whoPayFees;

    private boolean isNotificationFees;
    private double fraisTransfert;
    private String authID;
}
