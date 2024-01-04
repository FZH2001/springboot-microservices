package com.example.fundtransferservice.model.dto;

import java.math.BigDecimal;

public class TransactionRequest {
    private String donorId;
    private String beneficiaryId;
    private BigDecimal amount;
    private String whoPayFees;

    private boolean isNotificationFees;
    private double fraisTransfert;
    private String authID;
}
