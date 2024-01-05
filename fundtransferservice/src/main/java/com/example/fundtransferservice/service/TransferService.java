package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.TransactionStatus;
import com.example.fundtransferservice.model.TransactionType;
import com.example.fundtransferservice.model.dto.*;
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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferService {
    private final TransactionRepository transactionRepository;
    private final FeesCalculationService feesCalculationService;
    private final FundTransferRestClient fundTransferRestClient;
    private final Utils utils;
    private final NotificationService smsService;
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
        else if(transactionEntity.getPaymentType().equals(TransactionType.CASH) && transactionEntity.getAmount()>agentResponse.getSolde()){
            response=utils.buildFailedTransactionResponse(transactionEntity.getTransactionReference(),"Amount is greater than what agent has");
        }

        else{
            // ? : if the notify is true then we send SMS to the beneficiary
            if(transactionEntity.isNotify()){
                // TODO : send SMS to the beneficiary
                sendTransactionReferenceToBeneficiary(transactionEntity);
            }

            // ? : if the amount is valid then we proceed with Fees calculation
            feesCalculation(transactionEntity);

            // ? : we send an OTP to the client and to the Front
            String otp = generateOTP(6);
            sendOTP(transactionEntity,otp);

            // ? : we save the transaction in the database
            transactionRepository.save(transactionEntity);
            response=utils.buildSuccessfulTransactionResponse(transactionEntity);
            response.setGeneratedOTP(otp);
        }

        return response;
    }

    public TransactionResponse validateRestitution(TransactionRequest request){
        TransactionEntity transactionEntity = new TransactionEntity();
        BeanUtils.copyProperties(request,transactionEntity);
        TransactionResponse response = new TransactionResponse() ;
        TransactionEntity transactionToBeRestituted = transactionRepository.findByTransactionReference(transactionEntity.getTransactionReference());

        if (transactionToBeRestituted.getStatus().equals(TransactionStatus.PAID) || transactionToBeRestituted.getStatus().equals(TransactionStatus.BLOCKED)) {
            response = utils.buildFailedTransactionResponse(transactionEntity.getTransactionReference(), "Transaction already paid or blocked");

            return response;

        }
        else if(transactionToBeRestituted.getStatus().equals(TransactionStatus.TO_BE_SERVED) &&  transactionToBeRestituted.getAgentId().equals(transactionEntity.getAgentId()) ){
            // ? we update the transaction status to refunded
            transactionToBeRestituted.setStatus(TransactionStatus.REFUNDED);

            // ? we specify the reason of the refund
            transactionToBeRestituted.setRefundReason(transactionEntity.getRefundReason());

            // ? we update the agent solde
            if(transactionToBeRestituted.getPaymentType().equals(TransactionType.CASH)){

                double lastSolde = fundTransferRestClient.getAgentInfo(transactionToBeRestituted.getAgentId()).getSolde();
                fundTransferRestClient.updateAgentCredits(transactionToBeRestituted.getAgentId(),lastSolde+ transactionToBeRestituted.getAmount());

            }
            // ? we update the client solde
            else {
                double lastSolde = fundTransferRestClient.getClientInfo(transactionToBeRestituted.getDonorId()).getSolde();
                fundTransferRestClient.updateClientSolde(transactionToBeRestituted.getDonorId(),lastSolde+ transactionToBeRestituted.getAmount());
            }

            //? we notify the client that the transaction has been refunded
            if(transactionEntity.isNotify()){
                notifyClientWithRestitution(transactionToBeRestituted);
            }
            transactionRepository.save(transactionToBeRestituted);
            response  = utils.buildSuccessfulTransactionResponse(transactionToBeRestituted);
        }


        return response;
    }
    public void sendOTP(TransactionEntity entity,String otp){
        // TODO : send OTP to the client
        smsService.sendSMStoClient(new SMSRequest(fundTransferRestClient.getClientInfo(entity.getBeneficiaryId()).getGsm(),otp,fundTransferRestClient.getClientInfo(entity.getDonorId()).getPrenom()));

    }
    public void sendTransactionReferenceToBeneficiary(TransactionEntity entity){
        // TODO : send Transaction Reference to the beneficiary
        smsService.sendSMStoClient(new SMSRequest(fundTransferRestClient.getBeneficiaryInfo(entity.getBeneficiaryId()).getPhone(),entity.getTransactionReference(),fundTransferRestClient.getClientInfo(entity.getDonorId()).getPrenom()));

    }

    public void notifyClientWithRestitution(TransactionEntity entity){
        ClientResponse clientResponse = fundTransferRestClient.getClientInfo(entity.getDonorId());
        smsService.sendSMStoClient(new SMSRequest(clientResponse.getGsm(),"Votre transaction a ete restituee",clientResponse.getPrenom()));

    }

    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return otp.toString();
    }

    public void feesCalculation(TransactionEntity entity){



        if(entity.getWhoPayFees().equals("donor")){
            if(entity.getPaymentType().equals(TransactionType.CASH)){
                Map<String,Double> frais = feesCalculationService.calculFraisDonneurOrdre(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());

                Double lastSolde = fundTransferRestClient.getAgentInfo(entity.getAgentId()).getSolde();
                fundTransferRestClient.updateAgentCredits(entity.getAgentId(),lastSolde - frais.get("montantTotalOperation"));

                // TODO update benificary solde
            }else {
                Map<String,Double> frais =feesCalculationService.calculFraisDonneurOrdre(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
                Double lastSolde = fundTransferRestClient.getClientInfo(entity.getDonorId()).getSolde();
                fundTransferRestClient.updateClientSolde(entity.getDonorId(),lastSolde - frais.get("montantTotalOperation"));

                // TODO update benificary solde
            }
        }
        else if(entity.getWhoPayFees().equals("beneficiary")){
            if(entity.getPaymentType().equals(TransactionType.CASH)){
                Map<String,Double> frais = feesCalculationService.calculFraisBeneficiaire(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
                Double lastSolde = fundTransferRestClient.getAgentInfo(entity.getAgentId()).getSolde();
                fundTransferRestClient.updateAgentCredits(entity.getAgentId(),lastSolde - frais.get("montantTotalOperation"));

                // TODO update benificary solde
            }else {
                Map<String,Double> frais =feesCalculationService.calculFraisBeneficiaire(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
                Double lastSolde = fundTransferRestClient.getClientInfo(entity.getDonorId()).getSolde();
                fundTransferRestClient.updateClientSolde(entity.getDonorId(),lastSolde - frais.get("montantTotalOperation"));

                // TODO update benificary solde
            }
        }
        else if(entity.getWhoPayFees().equals("shared")){
            if(entity.getPaymentType().equals(TransactionType.CASH)){
                Map<String,Double> frais = feesCalculationService.calculFraisPartages(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
                Double lastSolde = fundTransferRestClient.getAgentInfo(entity.getAgentId()).getSolde();
                fundTransferRestClient.updateAgentCredits(entity.getAgentId(),lastSolde - frais.get("montantTotalOperation"));

                // TODO update benificary solde
            }else {
                Map<String,Double> frais = feesCalculationService.calculFraisPartages(entity.getAmount(),entity.getFraisTransfert(),entity.isNotificationFees());
                Double lastSolde = fundTransferRestClient.getClientInfo(entity.getDonorId()).getSolde();
                fundTransferRestClient.updateClientSolde(entity.getDonorId(),lastSolde - frais.get("montantTotalOperation"));

                // TODO update benificary solde
            }
        }
    }




}
