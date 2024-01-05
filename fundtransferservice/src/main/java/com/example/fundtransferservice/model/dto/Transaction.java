package com.example.fundtransferservice.model.dto;

import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
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
    private Long donorId;
    private Long beneficiaryId;
    private Long agentId;
    private ClientResponse clientResponse;
    private BeneficiaryResponse beneficiaryResponse;
}
