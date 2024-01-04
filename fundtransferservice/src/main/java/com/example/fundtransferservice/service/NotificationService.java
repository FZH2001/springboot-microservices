package com.example.fundtransferservice.service;

import com.example.fundtransferservice.model.dto.SMSRequest;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@PropertySource(value = "classpath:application.yml")
public class NotificationService {

//    @Value("${infobip.api.url}")
    private static String BASE_URL = "https://3g6k51.api.infobip.com";
//    @Value("${infobip.api.key}")
    private  static String API_KEY = "336a18ec0d02f481b32b3f49a34815fe-7af9e21e-c834-4a42-a0c1-a77dca6cdcd1";

    public void sendSMStoClient(SMSRequest smsRequestDTO){
        ApiClient client = initApiClient();

        SendSmsApi sendSmsApi = new SendSmsApi(client);
//        System.out.println("here");
        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from(smsRequestDTO.getSender())
                .addDestinationsItem(new SmsDestination().to(smsRequestDTO.getRecipient()))
                .text(smsRequestDTO.getMessage());

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(Collections.singletonList(smsMessage));

        try {
            SmsResponse smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
            System.out.println("Response body: " + smsResponse);
        } catch (ApiException e) {
            System.out.println("HTTP status code: " + e.getCode());
            System.out.println("Response body: " + e.getResponseBody());
        }
    }

    private static ApiClient initApiClient() {
        ApiClient client = new ApiClient();

        client.setApiKeyPrefix("App");
        client.setApiKey(API_KEY);
        client.setBasePath(BASE_URL);

        return client;
    }
}