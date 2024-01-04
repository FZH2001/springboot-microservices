package com.example.fundtransferservice.model.repository;

import com.example.fundtransferservice.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
    TransactionEntity findByTransactionReference(String transactionReference);
<<<<<<< HEAD
    //TransactionEntity findByTransactionReference(String transactionReference,String code);
=======
//    TransactionEntity findByTransactionReference(String transactionReference,String code);
>>>>>>> 1d5d0170d56545c983952a25793ed198a21b354a

}
