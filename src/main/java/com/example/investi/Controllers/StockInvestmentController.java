package com.example.investi.Controllers;
import com.example.investi.Entities.Account;
import com.example.investi.Entities.Investor;
import com.example.investi.Entities.StockInvestment;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.InvestorRepository;
import com.example.investi.Repositories.StockInvestmentRepository;
import com.example.investi.Services.InvestmentService;
import com.example.investi.Services.StockService;
import com.example.investi.Services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockInvestmentController {

    private Logger logger = LoggerFactory.getLogger(InvestmentService.class);

    @Autowired
    private InvestorRepository investorRepository;
    @Autowired
    private StockInvestmentRepository stockInvestmentRepository;
    @Autowired
    private StockService stockService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/stock/{symbol}")
    public ResponseEntity<Object> getStockData(@PathVariable String symbol) {
        Object stockData = stockService.getStockData(symbol);
        return ResponseEntity.ok(stockData);
    }


    @PostMapping("/buy")
    public StockInvestment buyStock(@RequestParam long investorId, @RequestParam String symbol, @RequestParam int quantity) {
        // Validate input
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        Account investorAccount = accountRepository.findByInvestor(investor);
        if(investorAccount == null){
            throw new RuntimeException("Investor account not found.");
        }

        BigDecimal stockPrice = stockService.getStockPrice(symbol);

        if (stockPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid stock price for symbol: " + symbol);
        }

        BigDecimal totalAmountInvested = stockPrice.multiply(BigDecimal.valueOf(quantity));

        // Check account balance
        if (investorAccount.getBalance() < totalAmountInvested.doubleValue()) {
            throw new RuntimeException("Insufficient funds.");
        }

        logger.info("Investor ID: {}, Symbol: {}, Quantity: {}", investorId, symbol, quantity);
        logger.info("Stock Price: {}, Total Amount Invested: {}", stockPrice, totalAmountInvested);

        // Create new investment record
        StockInvestment investment = new StockInvestment();
        investment.setInvestor(investor);
        investment.setStockSymbol(symbol);
        investment.setQuantity(quantity);
        investment.setPurchasePrice(stockPrice);
        investment.setAmountInvested(totalAmountInvested);
        investment.setPurchaseDate(new Date());


        StockInvestment savedInvestment = stockInvestmentRepository.save(investment);

        // Deduct funds from account
        investorAccount.setBalance(investorAccount.getBalance() - totalAmountInvested.doubleValue());
        accountRepository.save(investorAccount);

        // Create transaction
        transactionService.createTransaction(investorAccount.getId(), 9L, totalAmountInvested);

        return savedInvestment;
    }

}
