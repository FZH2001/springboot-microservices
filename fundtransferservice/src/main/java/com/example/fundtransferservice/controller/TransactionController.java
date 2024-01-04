package com.example.fundtransferservice.controller;

import com.example.fundtransferservice.model.dto.SMSRequest;
import com.example.fundtransferservice.model.dto.Transaction;
import com.example.fundtransferservice.model.dto.TransactionRequest;
import com.example.fundtransferservice.service.NotificationService;
import com.example.fundtransferservice.service.TransferReverseService;
import com.example.fundtransferservice.service.TransferSearchService;
import com.example.fundtransferservice.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransferSearchService transferSearchService;

    private final TransferService transferService;
    private final TransferReverseService transferReverseService;
    private final NotificationService smsService;

    /*@PostMapping
>>>>>>> 1d5d0170d56545c983952a25793ed198a21b354a
    public ResponseEntity sendFundTransfer(@RequestBody TransactionRequest transactionRequest) {
        log.info("Got fund transfer request from API {}", transactionRequest.toString());
        return ResponseEntity.ok(transferService.fundTransfer(transactionRequest));
    }*/
    @GetMapping
    public ResponseEntity readFundTransfers() {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.readAllTransfers());
    }
    // To get transaction by agent
    @GetMapping("/agent/{transactionReference}")
    public ResponseEntity readFundTransaction(@PathVariable String transactionReference) {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferSearchService.findTransactionByRefOnly(transactionReference));
    }
    // To get transaction by Wallet or GAB
    @PostMapping("/readFundTransfer")
    public ResponseEntity readFundTransaction(@RequestBody Map<String, String> requestParams) {
        log.info("Searching for transaction by reference code and SMS code");

        String referenceCode = requestParams.get("referenceCode");
        String smsCode = requestParams.get("smsCode");

        if (referenceCode != null && smsCode != null) {
            Transaction transaction = transferSearchService.findTransactionByCode(referenceCode, smsCode);

            if (transaction != null) {
                return ResponseEntity.ok(transaction);
            }
        }

        return ResponseEntity.notFound().build();
    }
    /*@PostMapping
    public ResponseEntity sendRefundRequest(@RequestBody Map<String, String> requestParams) {
        String referenceCode = requestParams.get("referenceCode");
        String refundMotive = requestParams.get("motive");
        String agentId = requestParams.get("agentId");
        return ResponseEntity.ok(transferReverseService.sendReverseRequest(referenceCode,refundMotive,agentId));
    }*/

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
    public ResponseEntity calculateFees(@RequestBody TransactionRequest transactionRequest) {
        log.info("Got fund transfer request from API {}", transactionRequest.toString());
        return ResponseEntity.ok(transferService.validateSubmission(transactionRequest));
    }
}
