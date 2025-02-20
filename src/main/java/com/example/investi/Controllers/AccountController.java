package com.example.investi.Controllers;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.AccountType;
import com.example.investi.Entities.Project;
import com.example.investi.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create/{accountType}")
    public ResponseEntity<Account> createAccount(
            @RequestBody Account account,
            @RequestParam(required = false) Long clientId, // Optional for Project accounts
            @PathVariable String accountType,
            @RequestParam(required = false) Long project) { // Required only for Project accounts

        // Normalize the accountType string to match enum format
        String normalizedAccountType = accountType.substring(0, 1).toUpperCase() + accountType.substring(1).toLowerCase();

        // Convert accountType string to enum
        AccountType type;
        try {
            type = AccountType.valueOf(normalizedAccountType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid account type: " + accountType);
        }

        // Validate that a Project is provided for Project accounts
        if (type == AccountType.Project && project == null) {
            throw new RuntimeException("A Project must be provided for a Project account.");
        }

        // Call the service to create the account
        Account createdAccount = accountService.createAccount(account, clientId, type, project);
        return ResponseEntity.ok(createdAccount);
    }


    @GetMapping("/list")
    public ResponseEntity<List<Account>> listAccounts(
            @RequestParam Long clientId,
            @RequestParam(required = false) String accountType) {

        Optional<AccountType> type = Optional.empty();
        if (accountType != null) {
            try {
                type = Optional.of(AccountType.valueOf(accountType.substring(0, 1).toUpperCase() + accountType.substring(1).toLowerCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid account type: " + accountType);
            }
        }

        List<Account> accounts = accountService.listAccounts(clientId, type);
        return ResponseEntity.ok(accounts);
    }
    @PutMapping("/update/{accountId}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long accountId,
            @RequestBody Account updatedAccount,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long ProjectId) {

        // Call the service to update the account
        Account updated = accountService.updateAccount(accountId, updatedAccount, clientId,ProjectId);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long accountId,
            @RequestParam(required = false) Long clientId) {

        // Call the service to delete the account
        accountService.deleteAccount(accountId, clientId);

        // Return a 204 No Content response
        return ResponseEntity.noContent().build();
    }

}