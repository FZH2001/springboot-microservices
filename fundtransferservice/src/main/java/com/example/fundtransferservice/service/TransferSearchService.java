package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionResponse;
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
    public List<Transaction> readAllTransfers() {
        return mapper.convertToDtoList(transactionRepository.findAll());
    }

    // ? : Find Transaction by reference : Agent for cash transactions
    public TransactionResponse findTransactionByRefOnly(String reference){
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(reference);
        // Build a transaction response
        if(transactionEntity==null){
            log.error("Transfer not found");
            return buildFailedTransactionResponse(reference,"Transaction not found");
        }
        else if(transactionEntity.getStatus().equals(TransactionStatus.BLOCKED) || transactionEntity.getStatus().equals(TransactionStatus.PAID)){
            log.error("Transfer is blocked or already paid");
            return buildFailedTransactionResponse(reference,"Transfer is blocked or already paid");
        }
        return buildSuccessfulTransactionResponse(transactionEntity);
    }
    // Find Transaction by reference and SMS code : for GAB and Wallet
    public TransactionResponse findTransactionByCode(String reference, String code){
        TransactionEntity transactionEntity = transactionRepository.findByTransactionReference(reference);
        if(integrationService.isCodeValid(code) && transactionEntity!=null) {
            return buildSuccessfulTransactionResponse(transactionEntity);
        }
        log.error("Code is invalid");
        return buildFailedTransactionResponse(reference,"Code is invalid");
    }
    // Build a successful transaction response
    public TransactionResponse buildSuccessfulTransactionResponse(TransactionEntity transactionentity){
        TransactionResponse transactionResponse=new TransactionResponse();
        Transaction transaction = mapper.convertToDto(transactionentity);
        fundTransferRestClient.loadData();
        // add client information to the transaction
        transaction.setClientResponse(fundTransferRestClient.getClientInfo(transaction.getDonorId()));
        // add beneficiary information to the transaction
        transaction.setBeneficiaryResponse(fundTransferRestClient.getBeneficiaryInfo(transaction.getBeneficiaryId()));
        // return transaction with all the information needed
        transactionResponse.setTransaction(transaction);
        transaction.setTransactionReference(transaction.getTransactionReference());
        transactionResponse.setMessage("Success");
        log.info("Success");
        return transactionResponse;
    }
    // Build a failed transaction response
    public TransactionResponse buildFailedTransactionResponse(String transactionId,String errorMessage){
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(transactionId);
        transactionResponse.setMessage(errorMessage);
        return transactionResponse;
    }

}
