package com.example.fundtransferservice.model.dto;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Utils {
    private final TransactionMapper mapper = new TransactionMapper();
    private final FundTransferRestClient fundTransferRestClient;
    TransactionResponse transactionResponse=new TransactionResponse();
    public TransactionResponse buildFailedTransactionResponse(String transactionId,String errorMessage){
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(transactionId);
        transactionResponse.setMessage(errorMessage);
        return transactionResponse;
    }
    public TransactionResponse buildSuccessfulTransactionResponse(TransactionEntity transactionentity){

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
}
