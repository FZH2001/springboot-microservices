package com.example.fundtransferservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationService {
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
    public void generateReceipt(String reference){
        //TODO
    }

}
