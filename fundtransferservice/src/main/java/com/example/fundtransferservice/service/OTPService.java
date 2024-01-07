package com.example.fundtransferservice.service;

import com.example.fundtransferservice.client.FundTransferRestClient;
import com.example.fundtransferservice.model.dto.SMSRequest;
import com.example.fundtransferservice.model.entity.OTP;
import com.example.fundtransferservice.model.entity.TransactionEntity;
import com.example.fundtransferservice.model.repository.OTPRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Data
public class OTPService {

    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private final NotificationService smsService;

    @Autowired
    private FundTransferRestClient fundTransferRestClient;
    public String generateOTP(int length,Long clientId) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(ThreadLocalRandom.current().nextInt(0, 10));
        }

        // System.out.println("Im sending OTP to the client");

        OTP otpEntity = new OTP();
        otpEntity.setOtpValue(otp.toString());
        otpRepository.save(otpEntity);

        //sendOTP(clientId,otpEntity.getOtp());
        return otpEntity.getOtpValue();

    }

    public boolean validateOTP(String otp){
        System.out.println(otpRepository.findAll());
        if(otpRepository.findByOtpValue(otp) !=null){
//            otpRepository.deleteByOtpValue(otp);
            System.out.println("OTP is valid");
            return true;
        }
        System.out.println("OTP is not valid");
        return false;
    }

    public void sendOTP(Long clientId, String otp){
        // TODO : send OTP to the client
        System.out.println("I am sending OTP to the client");
        smsService.sendSMStoClient(new SMSRequest(fundTransferRestClient.getClientInfo(clientId).getGsm(),otp,fundTransferRestClient.getClientInfo(clientId).getPrenom()));

    }
}
