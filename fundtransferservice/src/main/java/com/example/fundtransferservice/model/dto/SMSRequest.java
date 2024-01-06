package com.example.fundtransferservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SMSRequest {
    private String recipient;
    private String message;
    private String sender;
}
