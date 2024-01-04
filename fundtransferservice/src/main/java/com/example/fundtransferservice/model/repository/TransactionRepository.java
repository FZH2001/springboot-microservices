package com.example.fundtransferservice.model.repository;

import com.example.fundtransferservice.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
    TransactionEntity findByTransactionReference(String transactionReference);
    //TransactionEntity findByTransactionReference(String transactionReference,String code);

}
