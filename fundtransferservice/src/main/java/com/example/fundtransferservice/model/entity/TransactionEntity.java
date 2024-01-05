package com.example.fundtransferservice.model.entity;

import com.example.fundtransferservice.model.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.example.fundtransferservice.model.TransactionStatus;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionReference;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private TransactionType paymentType;
    private BigDecimal amount;
    private static final BigDecimal plafondMax = BigDecimal.valueOf(1000000);
    private Date issueDate;
    private Date expiryDate;
    private Long donorId;
    private Long beneficiaryId;
    private Long agentId;
    private boolean isNotificationFees;
    private double fraisTransfert;
    private String whoPayFees;

    public Number getPlafond() {
        return plafondMax;
    }
}
