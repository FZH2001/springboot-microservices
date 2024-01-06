package com.example.fundtransferservice.model.dto;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {

    private Long id;
    private String transactionReference;
    private TransactionStatus status;
    private TransactionType paymentType;
    private double amount;
    private static final BigDecimal plafondMax = BigDecimal.valueOf(1000000);
    private Date issueDate;
    private Date expiryDate;
    private Long donorId;
    private Long beneficiaryId;
    private Long agentId;
    private boolean isNotificationFees;
    private double fraisTransfert;
    private String whoPayFees;
    private String RefundReason;
    private ClientResponse clientResponse;
    private BeneficiaryResponse beneficiaryResponse;
}
