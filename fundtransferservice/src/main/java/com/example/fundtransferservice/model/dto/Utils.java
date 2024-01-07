package com.example.fundtransferservice.model.dto;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import feign.Client;
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
        // fundTransferRestClient.loadData();
        Transaction transaction = mapper.convertToDto(transactionentity);
        log.info("Status :"+transactionentity.getStatus());
        // add client information to the transaction
        ClientResponse clientResponse = fundTransferRestClient.getClientInfo(transaction.getDonorId());

        if(transactionentity.getPaymentType().equals(TransactionType.CASH)){
            clientResponse.setAgentResponse(fundTransferRestClient.getAgentInfo(transaction.getAgentId()));
        }
        transaction.setClientResponse(clientResponse);
        // add beneficiary information to the transaction
        transaction.setBeneficiaryResponse(fundTransferRestClient.getBeneficiaryInfo(transaction.getBeneficiaryId()));
        // return transaction with all the information needed
        transactionResponse.setTransaction(transaction);
        transactionResponse.setTransactionId(transactionentity.getTransactionReference());
        transaction.setTransactionReference(transaction.getTransactionReference());
        transactionResponse.setMessage("Success");
        log.info("Success");
        return transactionResponse;
    }
}
