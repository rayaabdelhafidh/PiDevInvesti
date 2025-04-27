package com.example.investi.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class FraudDetectionService {

   /* private final String PREDICTION_URL = "http://localhost:5000/predict"; // URL de votre service Flask

    public boolean detectFraud(String description, double montantDeclare, int gravite) {
        RestTemplate restTemplate = new RestTemplate();

        // Créer le payload de la requête
        String jsonRequest = String.format("{\"description\": \"%s\", \"montant_declare\": %.2f, \"gravite\": %d}",
                description, montantDeclare, gravite);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        // Appeler l'API Python
        ResponseEntity<String> response = restTemplate.exchange(
                PREDICTION_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        // Extraire la prédiction
        String responseBody = response.getBody();
        return Integer.parseInt(responseBody.replaceAll("[^0-9]", ""));
    }*/
}
