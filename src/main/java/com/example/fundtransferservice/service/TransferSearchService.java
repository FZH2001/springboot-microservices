package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Service
public class TransferSearchService {
    private final TransactionMapper mapper = new TransactionMapper();
    private final TransactionRepository transactionRepository;
    private final IntegrationService integrationService;
    public List<Transaction> readAllTransfers() {
        return mapper.convertToDtoList(transactionRepository.findAll());
    }

    // Find Transaction by reference : Agent for cash transactions
    public Transaction findTransactionByRefOnly(String reference){
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(reference);
        if(transactionEntity==null){
            log.error("Transfer not found");
            return null;
        }
        else if(transactionEntity.getStatus().equals(TransactionStatus.BLOCKED) || transactionEntity.getStatus().equals(TransactionStatus.PAID)){
            log.error("Transfer is blocked or already paid");
            return null;
        }
        return mapper.convertToDto(transactionEntity);
    }
    // Find Transaction by reference and SMS code : for GAB and Wallet
    public Transaction findTransactionByCode(String reference, String code){
        if(integrationService.isCodeValid(code)) {
            return mapper.convertToDto(transactionRepository.findByTransactionReference(reference));
        }
        log.error("Code is invalid");
        return null;

    }
}
