package com.example.investi.Controllers;

import com.example.investi.Entities.Transaction;
import com.example.investi.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> createTransaction(
            @RequestParam Long senderAccountId,
            @RequestParam Long receiverAccountId,
            @RequestParam BigDecimal amount) {

        // Call the service to create the transaction
        Transaction transaction = transactionService.createTransaction(senderAccountId, receiverAccountId, amount);
        return ResponseEntity.ok(transaction);
    }
}