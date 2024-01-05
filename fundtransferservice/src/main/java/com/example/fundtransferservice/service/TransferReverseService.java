package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferReverseService {
    private final TransactionRepository transactionRepository;
    private final IntegrationService integrationService;
    private final TransactionMapper mapper = new TransactionMapper();
    public Transaction sendReverseRequest(String referenceCode, String refundMotive, Long agentId) {
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(referenceCode);
        if(transactionEntity==null && refundMotive.isEmpty()){
            log.error("Transfer not found or motive is empty");
            return null;
        }
        else if(!transactionEntity.getIssueDate().equals(LocalDate.now())
                || transactionEntity.getStatus().equals(TransactionStatus.PAID)
                || transactionEntity.getStatus().equals(TransactionStatus.BLOCKED) ){
            log.error("It's too late now son , too late");
            return null;
        }
        else if(!transactionEntity.getAgentId().equals(agentId)){
            log.error("Find your agent son , this ain't the one");
            return null;
        }
        transactionEntity.setStatus(TransactionStatus.REVERSED);
        integrationService.updateAgentCredits(agentId,transactionEntity.getAmount(),"increment");
        integrationService.generateReceipt(referenceCode);
        return mapper.convertToDto(transactionEntity);

    }
}
