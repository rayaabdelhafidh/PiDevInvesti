package com.example.investi.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    @Value("${currencylayer.api.key}")
    private String apiKey;

    @Value("${currencylayer.api.url}")
    private String apiUrl;

    public double getExchangeRate(String fromCurrency, String toCurrency) {
        // Construire l'URL avec les paramètres nécessaires
        String url = apiUrl + "?access_key=" + apiKey + "&currencies=" + toCurrency + "&source=" + fromCurrency;

        // Appeler l'API
        RestTemplate restTemplate = new RestTemplate();
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);

        if (response == null || !response.isSuccess()) {
            throw new RuntimeException("Failed to fetch exchange rates.");
        }

        // Extraire le taux de change
        Map<String, Double> rates = response.getQuotes();
        String quoteKey = fromCurrency + toCurrency; // Exemple : "USDEUR"
        return rates.getOrDefault(quoteKey, 1.0); // Retourne 1.0 si le taux n'est pas trouvé
    }

    public Map<String, Double> convertToAllCurrencies(BigDecimal amount, String fromCurrency) {
        // Build the URL to fetch exchange rates
        String url = apiUrl + "?access_key=" + apiKey + "&source=" + fromCurrency;

        // Call the API to get exchange rates
        RestTemplate restTemplate = new RestTemplate();
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);

        if (response == null || !response.isSuccess()) {
            throw new RuntimeException("Failed to fetch exchange rates.");
        }

        // Convert the amount to all currencies
        Map<String, Double> rates = response.getQuotes();
        Map<String, Double> convertedAmounts = new HashMap<>();

        for (Map.Entry<String, Double> entry : rates.entrySet()) {
            String currencyCode = entry.getKey().replace(fromCurrency, ""); // Extract target currency code
            double rate = entry.getValue();
            double convertedAmount = amount.multiply(BigDecimal.valueOf(rate)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            convertedAmounts.put(currencyCode, convertedAmount);
        }

        return convertedAmounts;
    }

}