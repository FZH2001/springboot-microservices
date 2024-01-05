package com.example.fundtransferservice.service;

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
    public boolean isBeneficiaryBlocked(String Id){
        //TODO: Check SIRON microservice for beneficiary status
        return false;
    }
    public boolean isGabEmpty(){
        return false;
    }
    public boolean isCodeValid(String code){
        //TODO
        return false;
    }
    public void settleAgentCredit(String agentId, BigDecimal amount){
        //TODO : access agent info and update his credits
        // use this for extourne
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
    //TODO: Check beneficiary status ( is it blacklisted ? )
    //TODO : Sign a new client for Wallet account
    //TODO : access agent info and update his credits

}
