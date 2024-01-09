package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferAdminService {
    private final TransactionRepository transactionRepository;
    private final Utils utils;

    public TransactionResponse blockTransaction(String transactionRef) {
        log.info(transactionRef);
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(transactionRef);
        // Build a transaction response
        if(transactionEntity==null){
            log.error("Transfer not found");
            return utils.buildFailedTransactionResponse(transactionRef,"Transaction not found");
        }
        transactionEntity.setStatus(TransactionStatus.BLOCKED);
        transactionRepository.save(transactionEntity);
        return utils.buildSuccessfulTransactionResponse(transactionEntity);
    }

    public TransactionResponse unblockTransaction(String transactionRef) {
        log.info(transactionRef);
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(transactionRef);
        // Build a transaction response
        if(transactionEntity==null){
            log.error("Transfer not found");
            return utils.buildFailedTransactionResponse(transactionRef,"Transaction not found");
        }
        transactionEntity.setStatus(TransactionStatus.TO_BE_SERVED);
        transactionRepository.save(transactionEntity);
        return utils.buildSuccessfulTransactionResponse(transactionEntity);
    }
}