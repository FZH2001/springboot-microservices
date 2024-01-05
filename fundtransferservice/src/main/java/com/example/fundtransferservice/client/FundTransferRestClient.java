package com.example.fundtransferservice.client;

import com.example.fundtransferservice.model.rest.response.AgentResponse;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name ="userservice")
public interface FundTransferRestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/agent/get-agent-data/{id}/")
    AgentResponse getAgentInfo(@PathVariable("id") Long agentId);
    @RequestMapping(method = RequestMethod.GET, value = "/api/client/get-client-data/{id}")
    ClientResponse getClientInfo(@PathVariable("id") Long donorId);


    // Look up beneficiary to get his status
    @RequestMapping(method = RequestMethod.GET, value = "/api/beneficiary/get-beneficiary-data/{id}")
    BeneficiaryResponse getBeneficiaryInfo(@PathVariable("id") Long beneficiaryId);
    // Look up beneficiary by name ( nom , prénom )
    @RequestMapping(method = RequestMethod.POST, value = "/api/beneficiary/get-beneficiary-data")
    BeneficiaryResponse getBeneficiaryInfoByName(@RequestBody Map<String, Object> requestParams);
    // Look up beneficiary by his wallet code
    @RequestMapping(method = RequestMethod.POST, value = "/api/beneficiary/get-beneficiary-data")
    BeneficiaryResponse getBeneficiaryInfoByCode(@RequestBody Map<String, Object> requestParams);


    @RequestMapping(method = RequestMethod.GET, value = "/api/client" +
            "/loadData")
    String loadData();

    //TODO : Sign a new client for Wallet account
    //TODO : access agent info and update his credits
}
