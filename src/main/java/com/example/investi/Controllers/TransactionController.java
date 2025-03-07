package com.example.investi.Controllers;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.Client;
import com.example.investi.Entities.Transaction;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.ClientRepository;
import com.example.investi.Repositories.TransactionRepository;
import com.example.investi.Services.CurrencyService;
import com.example.investi.Services.SmsService;
import com.example.investi.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.html2pdf.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;
    @GetMapping(value = "/{clientId}/transactions/report/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTransactionReportPdf(
            @PathVariable Long clientId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String accountNumber) {

        // Step 1: Fetch the client
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Step 2: Fetch all accounts linked to the client
        List<Account> accounts = accountRepository.findByClientId(clientId);

        // Step 3: Build the HTML content for the PDF
        String htmlContent = transactionService.buildHtmlReport(client, accounts, startDate, endDate, accountNumber);

        // Step 4: Convert HTML to PDF
        byte[] pdfContent = transactionService.generatePdfFromHtml(htmlContent);

        // Step 5: Return the PDF as a response
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transaction-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }


    @PostMapping("/transfer")
    public ResponseEntity<Transaction> createTransaction(
            @RequestParam Long senderAccountId,
            @RequestParam Long receiverAccountId,
            @RequestParam BigDecimal amount) {

        // Call the service to create the transaction
        Transaction transaction = null;
        try {
            transaction = transactionService.createTransaction(senderAccountId, receiverAccountId, amount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(transaction);
    }
    @GetMapping("/convert-all")
    public ResponseEntity<Map<String, Double>> convertAmountToAllCurrencies(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency) {
        // Fetch all currency conversions
        Map<String, Double> convertedAmounts = currencyService.convertToAllCurrencies(amount, fromCurrency);

        return ResponseEntity.ok(convertedAmounts);
    }
}