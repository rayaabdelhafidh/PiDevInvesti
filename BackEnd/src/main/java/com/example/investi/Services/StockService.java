package com.example.investi.Services;

import com.example.investi.Entities.StockInvestment;
import com.example.investi.Repositories.StockInvestmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StockService  implements IStockService<StockInvestment,Integer>{
    @Autowired
    private StockInvestmentRepository stockInvestmentRepository;
    private static final String API_KEY = "PMKU9L32DW7SNMBM";
    private static final String BASE_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol={symbol}&interval=5min&apikey=" + API_KEY;

    // Static list of available stock symbols
    @Autowired
    private RestTemplate restTemplate;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    public void printApiKey() {
        System.out.println("API Key: " + apiKey);
    }

    public Object getStockData(String symbol) {
        String url = BASE_URL;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, symbol);
        return response.getBody();
    }


    public BigDecimal getStockPrice(String symbol) {
        String url = UriComponentsBuilder.fromUriString("https://www.alphavantage.co/query")
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);
            JSONObject globalQuote = json.getJSONObject("Global Quote");

            // check if the Global Quote object contains data
            if (globalQuote.isEmpty()) {
                log.error("No data found for symbol: {}", symbol);
                return BigDecimal.ZERO;
            }

            String priceString = globalQuote.getString("05. price");

            //check if the priceString is a valid number
            if (priceString == null || priceString.isEmpty()) {
                log.error("Price data is empty or null for symbol: {}", symbol);
                return BigDecimal.ZERO;
            }

            BigDecimal price = new BigDecimal(priceString);
            log.info("Stock price for symbol {}: {}", symbol, price);
            return price.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("Error fetching stock price for symbol {}: {}", symbol, e.getMessage());
            return BigDecimal.ZERO;
        }
    }


    @Override
    public StockInvestment save(StockInvestment entity) {
        return stockInvestmentRepository.save(entity);
    }

    @Override
    public StockInvestment add(StockInvestment entity) {
        return null;
    }

    @Override
    public Optional<StockInvestment> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public StockInvestment update(Integer integer, StockInvestment entity) {
        return null;
    }

    @Override
    public List<StockInvestment> findAll() {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(StockInvestment entity) {

    }
}
