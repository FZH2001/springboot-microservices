package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.rest.response.AgentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationService {
    public final ReceiptGeneratorService receiptGeneratorService;
    private final FundTransferRestClient fundTransferRestClient;

    public boolean isBeneficiaryBlocked(Long id) {
        return fundTransferRestClient.isBeneficiaryBlacklisted(id);
    }
    public boolean isGabEmpty(){
        return false;
    }
    public boolean isCodeValid(String code){
        //TODO
        return false;
    }
    public void updateAgentCredits(Long agentId, double amount, String operation){
        AgentResponse agentResponse = fundTransferRestClient.getAgentInfo(agentId);
        // find agent to update
        if(operation.equals("increment")) {
            agentResponse.setSolde(agentResponse.getSolde()+amount);
        }
        else {
            agentResponse.setSolde(agentResponse.getSolde()-amount);
        }
        fundTransferRestClient.updateAgentCredits(agentResponse);

    }


    public void settleClientBalance(String clientId, BigDecimal fees){
        //TODO : access client info and update his credits
        // use this for Submit
    }
    public void generateReceipt(String reference){
        try {
            receiptGeneratorService.createDocument();
            receiptGeneratorService.createHeaderDetails(reference);
        }
        catch(FileNotFoundException fileNotFoundException){
            log.error(fileNotFoundException.getMessage());
        }
        }
    // Settle payment or block process
    //TODO : Look up beneficiary by name for agent or GAB
    //TODO : Look up beneficiary by Wallet code
    //TODO : Sign a new client for Wallet account
    //TODO : access agent info and update his credits

}
