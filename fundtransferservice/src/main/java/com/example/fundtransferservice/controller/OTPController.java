package com.example.fundtransferservice.controller;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = {"http://localhost:4200","https://main--remarkable-starlight-5f7dc0.netlify.app","https://main--unique-moxie-e9385c.netlify.app/"})
public class OTPController {

    @Autowired
    private OTPService otpService;

    @GetMapping("/send-otp/{clientId}")
    public String generateOTP(@PathVariable Long clientId){
        System.out.println("I am in the controller");
        return otpService.generateOTP(6,clientId);
    }
}
