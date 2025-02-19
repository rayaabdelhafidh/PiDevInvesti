package com.example.investi.Services;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.Transaction;
import com.example.investi.Entities.TransactionStatus;
import com.example.investi.Entities.TransactionType;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transaction createTransaction(Long senderAccountId, Long receiverAccountId, BigDecimal amount) {
        // Fetch the sender's account
        Account senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found with ID: " + senderAccountId));

        // Fetch the receiver's account
        Account receiverAccount = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found with ID: " + receiverAccountId));

        // Validate sufficient balance in the sender's account
        if (senderAccount.getBalance()-amount.doubleValue() < 0) {
            throw new RuntimeException("Insufficient balance in the sender's account.");
        }

        // Deduct the amount from the sender's account
        senderAccount.setBalance(senderAccount.getBalance()-amount.doubleValue());

        // Add the amount to the receiver's account
        receiverAccount.setBalance(receiverAccount.getBalance()+amount.doubleValue());

        // Save the updated account balances
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Create the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFERT);
        transaction.setDescription("Transfer from account " + senderAccountId + " to account " + receiverAccountId);
        transaction.setStatus(TransactionStatus.VALIDEE);
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);

        // Save the transaction
        return transactionRepository.save(transaction);
    }
}