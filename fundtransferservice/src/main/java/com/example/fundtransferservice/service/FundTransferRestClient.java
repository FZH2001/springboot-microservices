package com.example.fundtransferservice.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "fund-transfer-service")
public interface FundTransferRestClient {
}
