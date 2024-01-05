package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.mapper.TransactionMapper;
import com.example.fundtransferservice.model.repository.TransactionRepository;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferValidationService {
    private final TransactionRepository transactionRepository;
    private final IntegrationService integrationService;
    private final FundTransferRestClient fundTransferRestClient;
    private final Utils utils;

    public TransactionResponse validatePayment(String reference){

        TransactionEntity transactionEntity =transactionRepository.findByTransactionReference(reference);
        BeneficiaryResponse beneficiaryResponse = fundTransferRestClient.getBeneficiaryInfo(transactionEntity.getDonorId());
        if(transactionEntity!=null){
            if(integrationService.isBeneficiaryBlocked(transactionEntity.getBeneficiaryId())
                    || (transactionEntity.getPaymentType().equals(TransactionType.GAB) && integrationService.isGabEmpty())
                    || transactionEntity.getStatus().equals(TransactionStatus.PAID)
                    || transactionEntity.getStatus().equals(TransactionStatus.BLOCKED)){

                log.info("Payment is blocked");
                transactionEntity.setStatus(TransactionStatus.BLOCKED);
                transactionRepository.save(transactionEntity);
                return utils.buildFailedTransactionResponse(reference,"Payment is blocked");
            }
            /*else if(transactionEntity.getPaymentType().equals(TransactionType.WALLET) && !beneficiaryResponse.isHasWallet()){
                fundTransferRestClient.saveOrUpdateBeneficiary(beneficiaryResponse);
                return utils.buildFailedTransactionResponse(reference,"Beneficiary need a wallet account");
            }*/
            else {
                log.info("Payment is settled");
                integrationService.updateAgentCredits(transactionEntity.getAgentId(),transactionEntity.getAmount(),"decrement");
                transactionEntity.setStatus(TransactionStatus.PAID);
                transactionRepository.save(transactionEntity);
                //integrationService.generateReceipt(reference);
                return utils.buildSuccessfulTransactionResponse(transactionEntity);
            }
        }
        else {
            return utils.buildFailedTransactionResponse(reference,"Transaction not found");
        }
    }


}
