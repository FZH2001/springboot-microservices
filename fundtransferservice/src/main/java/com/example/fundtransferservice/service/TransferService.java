package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionRequest;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import com.example.fundtransferservice.model.rest.response.AgentResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferService {
    private final TransactionRepository transactionRepository;
    private final FeesCalculationService feesCalculationService;
    private final FundTransferRestClient fundTransferRestClient;
    private final Utils utils;
    private TransactionMapper mapper = new TransactionMapper();


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


    public TransactionResponse  validateSubmission(TransactionRequest request){

        // ? : checks if amount is greater than plafond
        TransactionEntity transactionEntity = new TransactionEntity();
        BeanUtils.copyProperties(request,transactionEntity);
        ClientResponse clientResponse = fundTransferRestClient.getClientInfo(transactionEntity.getDonorId());
        AgentResponse agentResponse = fundTransferRestClient.getAgentInfo(transactionEntity.getAgentId());
        transactionEntity.setTransactionReference(UUID.randomUUID().toString());
        TransactionResponse response = new TransactionResponse();
        if(transactionEntity.getAmount() < transactionEntity.getPlafond().doubleValue()){
            response=utils.buildFailedTransactionResponse(transactionEntity.getTransactionReference(),"Amount is greater than plafond");
        }
        // TODO :  check if amount is greater than Client Balance ( for Mobile and Debit de Compte )
        else if((transactionEntity.getPaymentType().equals(TransactionType.WALLET)
                || transactionEntity.getPaymentType().equals(TransactionType.GAB)) && transactionEntity.getAmount()>clientResponse.getSolde()){
            response=utils.buildFailedTransactionResponse(transactionEntity.getTransactionReference(),"Amount is greater than what donor has");

        }
        // TODO : check if amount is greater than Agent Balance ( for CASH )
        else if(transactionEntity.getPaymentType().equals(TransactionType.CASH)
                && transactionEntity.getAmount()>agentResponse.getSolde()){
            response=utils.buildFailedTransactionResponse(transactionEntity.getTransactionReference(),"Amount is greater than what agent has");
        }

        else{
            // ? : if the amount is valid then we proceed with Fees calculation
            feesCalculation(transactionEntity);
            transactionRepository.save(transactionEntity);
            response=utils.buildSuccessfulTransactionResponse(transactionEntity);
        }

        return response;
    }

    public void feesCalculation(TransactionEntity entity){
        if(entity.getWhoPayFees().equals("donor")){
            feesCalculationService.calculFraisDonneurOrdre(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
        }
        else if(entity.getWhoPayFees().equals("beneficiary")){
            feesCalculationService.calculFraisBeneficiaire(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
        }
        else if(entity.getWhoPayFees().equals("shared")){
            feesCalculationService.calculFraisPartages(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
        }
    }




}
