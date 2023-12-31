package com.example.fundtransferservice.model.dto;

import lombok.Data;

@Data
public class SMSRequest {
    private String recipient;
    private String message;
    private String sender;
}
