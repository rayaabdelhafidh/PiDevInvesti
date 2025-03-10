package com.example.investi.Services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Value("${mailjet.sender.email}")
    private String senderEmail;

    public void sendEmail(String toEmail, String subject, String message) {
        try {

             MailjetClient client = new MailjetClient(ClientOptions.builder()
            .apiKey(apiKey)
            .apiSecretKey(apiSecret)
            .build());

            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put("From", new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", "Investi"))
                                    .put("To", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", toEmail)
                                                    .put("Name", "Recipient")))
                                    .put("Subject", subject)
                                    .put("TextPart", message)
                                    .put("HTMLPart", "<h3>" + message + "</h3>")
                            ));

            MailjetResponse response = client.post(request);

            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Data: " + response.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
