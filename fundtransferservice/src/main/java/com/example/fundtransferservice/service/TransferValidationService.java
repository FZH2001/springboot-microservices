package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.model.dto.Utils;
import com.example.fundtransferservice.model.entity.TransactionEntity;
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
        TransactionResponse transactionResponse = new TransactionResponse();
        //noinspection ConstantValue
        if(transactionEntity!=null){

            if(integrationService.isBeneficiaryBlackListed(transactionEntity.getBeneficiaryId())
                    || (transactionEntity.getPaymentType().equals(TransactionType.GAB) && integrationService.isGabEmpty())){
                log.info("Payment is blocked");
                transactionEntity.setStatus(TransactionStatus.BLOCKED);
                transactionRepository.save(transactionEntity);
                transactionResponse=utils.buildFailedTransactionResponse(reference,"Payment is blocked");
            }
            else if(transactionEntity.getStatus().equals(TransactionStatus.PAID) || transactionEntity.getStatus().equals(TransactionStatus.BLOCKED)){
                transactionResponse=utils.buildFailedTransactionResponse(reference,"Payment is already paid or blocked");
            }
            else if(transactionEntity.getPaymentType().equals(TransactionType.WALLET)
                    &&!integrationService.beneficiaryHasWallet(beneficiaryResponse.getId())){
                transactionResponse=utils.buildFailedTransactionResponse(reference,"Beneficiary needs wallet account");
            }
            else {
                log.info("Payment is settled");
                if(integrationService.updateAgentCredits(transactionEntity.getAgentId(),transactionEntity.getAmount(),"decrement")) {
                    transactionEntity.setStatus(TransactionStatus.PAID);
                    transactionRepository.save(transactionEntity);
                    if (transactionEntity.getPaymentType().equals(TransactionType.WALLET)) {
                        System.out.println(beneficiaryResponse.getWalletClient());
                        integrationService.updateClientCredits(beneficiaryResponse.getWalletClient(), transactionEntity.getAmount(), "increment");
                    }
                    transactionResponse = utils.buildSuccessfulTransactionResponse(transactionEntity);
                }
                else {
                    transactionResponse = utils.buildFailedTransactionResponse(reference,"Agent went broke");
                }
            }
        }

        else {
            transactionResponse=utils.buildFailedTransactionResponse(reference,"Transaction not found");
        }
        return transactionResponse;
    }


}
