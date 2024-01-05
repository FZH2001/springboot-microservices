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
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransferSearchService transferSearchService;
    private final TransferService transferService;
    private final TransferReverseService transferReverseService;
    private final TransferValidationService transferValidationService;
    private final NotificationService smsService;
    @GetMapping
    public ResponseEntity<List<Transaction>> readFundTransfers() {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.readAllTransfers());
    }
    // TODO : send and verify OTP
    @GetMapping("/agent/validate/{transactionReference}")
    public ResponseEntity<TransactionResponse> validatePayment(@PathVariable String transactionReference){
        return ResponseEntity.ok(transferValidationService.validatePayment(transactionReference));
    }
    // To get transaction by agent
    @GetMapping("/agent/{transactionReference}")
    public ResponseEntity<TransactionResponse> readFundTransaction(@PathVariable String transactionReference) {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.findTransactionByRefOnly(transactionReference));
    }
    // To get transaction by Wallet or GAB
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

    @PostMapping("/SubmitTransaction")
    public ResponseEntity<TransactionResponse> calculateFees(@RequestBody TransactionRequest transactionRequest) {
        log.info("Got fund transfer request from API {}", transactionRequest.toString());
        return ResponseEntity.ok(transferService.validateSubmission(transactionRequest));
    }
}
