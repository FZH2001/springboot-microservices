package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
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
    private final FundTransferRestClient fundTransferRestClient;
    private final Utils utils;
    public List<Transaction> readAllTransfers() {
        return mapper.convertToDtoList(transactionRepository.findAll());
    }

    // ? : Find Transaction by reference : Agent for cash transactions
    public TransactionResponse findTransactionByRefOnly(String reference){
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(reference);
        // Build a transaction response
        if(transactionEntity==null){
            log.error("Transfer not found");
            return utils.buildFailedTransactionResponse(reference,"Transaction not found");
        }
        else if(transactionEntity.getStatus().equals(TransactionStatus.BLOCKED) || transactionEntity.getStatus().equals(TransactionStatus.PAID)){
            log.error("Transfer is blocked or already paid");
            return utils.buildFailedTransactionResponse(reference,"Transfer is blocked or already paid");
        }
        return utils.buildSuccessfulTransactionResponse(transactionEntity);
    }
    // Find Transaction by reference and SMS code : for GAB and Wallet
    public TransactionResponse findTransactionByCode(String reference, String code){
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(reference);
        if(integrationService.isCodeValid(code) && transactionEntity!=null) {
            return utils.buildSuccessfulTransactionResponse(transactionEntity);
        }
        log.error("Code is invalid");
        return utils.buildFailedTransactionResponse(reference,"Code is invalid");
    }
    // Build a successful transaction response


}
