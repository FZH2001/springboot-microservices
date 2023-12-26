package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferValidationService {
    private final TransactionMapper mapper = new TransactionMapper();
    private final TransactionRepository transactionRepository;
    private final IntegrationService integrationService;
    public Transaction validatePayment(String reference){

        TransactionEntity transactionEntity =transactionRepository.findByTransactionReference(reference);
        if(transactionEntity!=null){
            if(integrationService.isBeneficiaryBlocked(transactionEntity.getBeneficiaryId()) ||
                    (transactionEntity.getPaymentType().equals(TransactionType.GAB) && integrationService.isGabEmpty())){
                log.error("Payment is blocked");
                transactionEntity.setStatus(TransactionStatus.BLOCKED);
                transactionRepository.save(transactionEntity);
            }
            else {
                log.error("Payment is settled");
                transactionEntity.setStatus(TransactionStatus.PAID);
                transactionRepository.save(transactionEntity);
            }
        }
        return mapper.convertToDto(transactionEntity);
    }

}
