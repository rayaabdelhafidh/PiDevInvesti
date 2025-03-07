package com.example.investi.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SMSService {
    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone.number}")
    private String OUTGOING_SMS_NUMBER;


    @PostConstruct
    private void setIDandTOKEN(){
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
    }

    public String sendSMS(String smsNum,String smsMessage){
        Message message = Message.creator(
                new PhoneNumber("+216".concat(smsNum)),
                new PhoneNumber(OUTGOING_SMS_NUMBER),smsMessage).create();
        return message.getStatus().toString();
    }
}
