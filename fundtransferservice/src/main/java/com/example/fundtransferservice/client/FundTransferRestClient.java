package com.example.fundtransferservice.client;

import com.example.fundtransferservice.model.rest.response.AgentResponse;
import com.example.fundtransferservice.model.rest.response.BeneficiaryResponse;
import com.example.fundtransferservice.model.rest.response.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name ="user-service")
public interface FundTransferRestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/agents/get-agent-data/{id}")
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
    @RequestMapping(method = RequestMethod.GET, value = "/api/client/beneficiaire/blocklisted/{id}")
    Boolean isBeneficiaryBlacklisted(@PathVariable Long id);
    @RequestMapping(method = RequestMethod.PUT, value = "/api/agents/update-agent-solde/{id}")
    String updateAgentCredits(@PathVariable Long id,@RequestParam Double newSolde);


    @RequestMapping(method = RequestMethod.PUT, value = "/api/client/update-client-solde/{id}")
    ResponseEntity<String> updateClientSolde(@PathVariable Long id, @RequestParam Double newSolde);


    @RequestMapping(method = RequestMethod.GET, value = "/api/client/loadData")
    String loadData();

    @RequestMapping(method = RequestMethod.POST, value = "/api/client/add-beneficiary")
    ResponseEntity<String> createBeneficiary(@RequestBody BeneficiaryResponse beneficiaryResponse);
    @RequestMapping(method = RequestMethod.POST, value = "/api/agents/add-client/{benId}")
    ResponseEntity<Object> createBeneficiaireClient(@PathVariable Long benId,@RequestBody ClientResponse c);
    @RequestMapping(method = RequestMethod.POST, value = "/api/client/add-client")
    ResponseEntity<String> saveOrUpdateClient(@RequestBody ClientResponse client);
}
