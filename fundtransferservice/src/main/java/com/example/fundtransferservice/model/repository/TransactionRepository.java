package com.example.fundtransferservice.model.repository;

import com.example.fundtransferservice.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
    TransactionEntity findByTransactionReference(String transactionReference);
    TransactionEntity findByAgentId(Long agentId);

    List<TransactionEntity> findByDonorId(Long donorId);
//    TransactionEntity findByTransactionReference(String transactionReference,String code);
}
