package com.example.fundtransferservice.controller;

import com.example.fundtransferservice.model.dto.TransactionRequest;
import com.example.fundtransferservice.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {
    private final TransferService transferService;
    @PostMapping
    public ResponseEntity sendFundTransfer(@RequestBody TransactionRequest transactionRequest) {
        log.info("Got fund transfer request from API {}", transactionRequest.toString());
        return ResponseEntity.ok(transferService.fundTransfer(transactionRequest));
    }
    @GetMapping
    public ResponseEntity readFundTransfers () {
        log.info("Reading fund transfers from core");
        return ResponseEntity.ok(transferService.readAllTransfers());
    }
}
