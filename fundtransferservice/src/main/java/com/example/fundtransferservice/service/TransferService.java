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
    public List<Transaction> readAllTransfers() {
        return mapper.convertToDtoList(transactionRepository.findAll());
    }


    public TransactionResponse  validateSubmission(TransactionRequest request){

        // ? : checks if amount is greater than plafond
        TransactionEntity entity = new TransactionEntity();
        BeanUtils.copyProperties(request,entity);
        TransactionResponse response = new TransactionResponse();
        if(entity.getAmount().doubleValue() > entity.getPlafond().doubleValue()){
            response.setMessage("Amount is greater than plafond");
            response.setTransactionId(UUID.randomUUID().toString());
        }
        // TODO :  check if amount is greater than Client Balance ( for Mobile and Debit de Compte )

        // TODO : check if amount is greater than Agent Balance ( for CASH )
        else{
            // ? : if the amount is valid then we proceed with Fees calculation
            feesCalculation(entity);

            response.setMessage("Success");
            response.setTransactionId(UUID.randomUUID().toString());
        }

        return response;
    }

    public void feesCalculation(TransactionEntity entity){
        if(entity.getWhoPayFees().equals("donor")){
            feesCalculationService.calculFraisDonneurOrdre(entity.getAmount().doubleValue(),entity.getFraisTransfert(),entity.isNotificationFees());
        }
        else if(entity.getWhoPayFees().equals("beneficiary")){
            feesCalculationService.calculFraisBeneficiaire(entity.getAmount().doubleValue(),entity.getFraisTransfert(),entity.isNotificationFees());
        }
        else if(entity.getWhoPayFees().equals("shared")){
            feesCalculationService.calculFraisPartages(entity.getAmount().doubleValue(),entity.getFraisTransfert(),entity.isNotificationFees());
        }
    }




}
