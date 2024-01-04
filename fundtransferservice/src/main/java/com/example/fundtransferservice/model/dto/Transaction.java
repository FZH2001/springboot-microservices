package com.example.fundtransferservice.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Transaction {
    private Long id;
    private String transactionReference;
    private String status;
    private String type;
    private BigDecimal amount;
    private BigDecimal plafondMax;
    private Date issueDate;
    private Date expiryDate;
    private String donorId;
    private String beneficiaryId;
}
