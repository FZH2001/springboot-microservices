package com.example.fundtransferservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "fund-transfer-service")
public interface FundTransferRestClient {
    // Settle payment or block process
    //TODO: COMMUNICATE WITH OTHER MICROSERVICE
    //TODO : Look up beneficiary by name for agent or GAB
    //TODO : Look up beneficiary by Wallet code
    //TODO: Check beneficiary status ( is it blacklisted ? )
    //TODO : Sign a new client for Wallet account
    //TODO : access agent info and update his credits
}
