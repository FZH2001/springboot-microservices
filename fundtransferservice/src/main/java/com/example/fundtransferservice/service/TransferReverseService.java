package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferReverseService {
    private final TransactionRepository transactionRepository;
    private final IntegrationService integrationService;
    private final Utils utils;
    public TransactionResponse sendReverseRequest(String referenceCode, String refundMotive, Long agentId) {
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(referenceCode);
        LocalDate issueLocalDate = transactionEntity.getIssueDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate today = LocalDate.now();
        boolean isSameDay = (issueLocalDate.getDayOfMonth() == today.getDayOfMonth()) &&
                (issueLocalDate.getMonth() == today.getMonth()) &&
                (issueLocalDate.getYear() == today.getYear());
        if(transactionEntity==null || refundMotive.isEmpty()){
            log.error("Transfer not found or motive is empty");
            return utils.buildFailedTransactionResponse(referenceCode,"transfer not found or motive is empty");
        }
        else if(!isSameDay || transactionEntity.getStatus().equals(TransactionStatus.PAID)
                || transactionEntity.getStatus().equals(TransactionStatus.BLOCKED)
                || transactionEntity.getStatus().equals(TransactionStatus.REVERSED)){
            log.error("It's too late now son , too late");
            return utils.buildFailedTransactionResponse(referenceCode,"time for reverse has expired");
        }
        else if(!transactionEntity.getAgentId().equals(agentId)){
            log.error("Find your agent son , this ain't the one");
            return utils.buildFailedTransactionResponse(referenceCode,"Find your agent son , this ain't the one");
        }
        transactionEntity.setStatus(TransactionStatus.REVERSED);
        integrationService.updateAgentCredits(agentId,transactionEntity.getAmount()+transactionEntity.getFraisTransfert(),"increment");
        transactionRepository.save(transactionEntity);
       return utils.buildSuccessfulTransactionResponse(transactionEntity);


    }
}
