package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.rest.response.AgentResponse;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
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

    public boolean isBeneficiaryBlackListed(Long id) {
        return fundTransferRestClient.isBeneficiaryBlacklisted(id);
    }
    public boolean isGabEmpty(){
        return false;
    }
    public boolean isCodeValid(String code){
        //TODO
        return false;
    }
    public boolean beneficiaryHasWallet(Long benId){
        BeneficiaryResponse beneficiaryResponse = fundTransferRestClient.getBeneficiaryInfo(benId);
        return !beneficiaryResponse.getWalletCode().isEmpty();
    }
    public ClientResponse createWalletClient(BeneficiaryResponse beneficiaryResponse){
        ClientResponse beneficiaryWallet = new ClientResponse();
        beneficiaryWallet.setPrenom(beneficiaryResponse.getPrenom());
        beneficiaryWallet.setTitle(beneficiaryResponse.getNom());
        beneficiaryWallet.setEmail(beneficiaryResponse.getEmail());
        beneficiaryWallet.setGsm(beneficiaryResponse.getPhone());
        return beneficiaryWallet;
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

    public void updateClientCredits(Long clientId, double amount, String operation){
        ClientResponse client = fundTransferRestClient.getClientInfo(clientId);
        double newAgentSolde = client.getSolde();
        // find agent to update
        if(operation.equals("increment")) {
            newAgentSolde+=amount;
        }
        else if(operation.equals("decrement")){
            newAgentSolde-=amount;
        }
        fundTransferRestClient.updateAgentCredits(clientId,newAgentSolde);

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
            receiptGeneratorService.createAddress("Anas","Marrakech","anasssaidi1337@gmail.com");
        }
        catch(FileNotFoundException fileNotFoundException){
            log.error(fileNotFoundException.getMessage());
        }
    }


}
