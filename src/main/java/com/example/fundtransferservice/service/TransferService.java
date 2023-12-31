package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionRequest;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferService {
    private final TransactionRepository transactionRepository;
    public TransactionResponse fundTransfer(TransactionRequest request) {
        log.info("Sending fund transfer request {}" + request.toString());
        TransactionEntity entity = new TransactionEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setStatus(TransactionStatus.TO_BE_SERVED);
        TransactionEntity optTransaction = transactionRepository.save(entity);
        TransactionResponse fundTransferResponse = new TransactionResponse();
        fundTransferResponse.setTransactionId(UUID.randomUUID().toString());
        fundTransferResponse.setMessage("Success");
        return fundTransferResponse;
    }




}
