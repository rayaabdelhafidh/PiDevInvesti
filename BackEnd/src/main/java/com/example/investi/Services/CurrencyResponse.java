package com.example.investi.Services;

import java.util.Map;

public class CurrencyResponse {
    private boolean success;
    private Map<String, Double> quotes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Double> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, Double> quotes) {
        this.quotes = quotes;
    }
}
