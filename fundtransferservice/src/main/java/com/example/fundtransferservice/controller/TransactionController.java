package com.example.fundtransferservice.controller;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.dto.SMSRequest;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionRequest;
import com.example.fundtransferservice.model.dto.TransactionResponse;
import com.example.fundtransferservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://main--remarkable-starlight-5f7dc0.netlify.app","https://main--unique-moxie-e9385c.netlify.app/","https://659d28735cac669fa793e38d--chic-mousse-aa1b3d.netlify.app/"})
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransferSearchService transferSearchService;
    private final TransferService transferService;
    private final TransferReverseService transferReverseService;
    private final TransferValidationService transferValidationService;
    private final NotificationService smsService;
    private final TransferAdminService transferAdminService;

    @GetMapping("/{donorId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDonorId(@PathVariable Long donorId) {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.getAllTransactionsById(donorId));
    }
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> readFundTransfers() {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.readAllTransfers());
    }
    // TODO : send and verify OTP
    @PostMapping("/agent/validate/{transactionReference}")
    public ResponseEntity<TransactionResponse> validatePayment(@PathVariable String transactionReference,@RequestBody String otp){
        return ResponseEntity.ok(transferValidationService.validatePayment(transactionReference,otp));
    }
    // To get transaction by agent or wallet
    @GetMapping("/agent/{transactionReference}")
    public ResponseEntity<TransactionResponse> readFundTransaction(@PathVariable String transactionReference) {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.findTransactionByRefOnly(transactionReference));
    }
    // To get transaction by gab
    @PostMapping("/readFundTransfer")
    public ResponseEntity<TransactionResponse> readFundTransaction(@RequestBody Map<String, String> requestParams) {
        log.info("Searching for transaction by reference code and SMS code");

        String referenceCode = requestParams.get("referenceCode");
        String smsCode = requestParams.get("smsCode");

        if (referenceCode != null && smsCode != null) {
            TransactionResponse transactionResponse = transferSearchService.findTransactionByCode(referenceCode, smsCode);

            if (transactionResponse != null) {
                return ResponseEntity.ok(transactionResponse);
            }
        }

        return ResponseEntity.notFound().build();
    }
    @PostMapping("/agent/reverse")
    public ResponseEntity<TransactionResponse> sendReverseRequest(@RequestBody Map<String, String> requestParams) {
        String referenceCode = requestParams.get("referenceCode");
        String refundMotive = requestParams.get("motive");
        Long agentId = Long.valueOf(requestParams.get("agentId"));
        return ResponseEntity.ok(transferReverseService.sendReverseRequest(referenceCode,refundMotive,agentId));
    }

    @PostMapping("/sendSMStoClient")
    public ResponseEntity<String> sendSMSToClient(@RequestBody SMSRequest smsRequestDTO) {
        try {
            smsService.sendSMStoClient(smsRequestDTO);
            return ResponseEntity.ok("SMS sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS: " + Arrays.toString(e.getStackTrace()));
        }
    }

    @PostMapping("/agent/submitTransaction")
    public ResponseEntity<TransactionResponse> submitTransaction(@RequestBody TransactionRequest transactionRequest) {
        log.info("Got fund transfer request from API {}", transactionRequest.toString());
        return ResponseEntity.ok(transferService.validateSubmission(transactionRequest));
    }

    @PostMapping("/restituerTransaction")
    public ResponseEntity<TransactionResponse> restituerTransaction(@RequestBody TransactionRequest transactionRequest) {
        log.info("Got fund transfer request from API {}", transactionRequest.toString());
        return ResponseEntity.ok(transferService.validateRestitution(transactionRequest));
    }
    @PostMapping("/agent/block")
    public ResponseEntity<TransactionResponse> blockTransaction(@RequestBody Map<String, String> requestParams){
        return ResponseEntity.ok(transferAdminService.blockTransaction(requestParams.get("reference")));
    }
    @PostMapping("/agent/unblock")
    public ResponseEntity<TransactionResponse> unblockTransaction(@RequestBody Map<String, String> requestParams){
        return ResponseEntity.ok(transferAdminService.unblockTransaction(requestParams.get("reference")));
    }
}
