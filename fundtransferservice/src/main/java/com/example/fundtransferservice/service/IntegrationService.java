package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.rest.response.AgentResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
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
        double newAgentSolde = agentResponse.getSolde();
        // find agent to update
        if(operation.equals("increment")) {
            newAgentSolde+=amount;
        }
        else if(operation.equals("decrement")){
            newAgentSolde-=amount;
        }
        fundTransferRestClient.updateAgentCredits(agentId,newAgentSolde);

    }


    public void settleClientBalance(String clientId, BigDecimal fees){
        //TODO : access client info and update his credits
        // use this for Submit
    }
        public void createNewClient(ClientResponse clientResponse){
            //TODO : Sign a new client for Wallet account
        //fundTransferRestClient.createNewClient(clientResponse);
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


}
