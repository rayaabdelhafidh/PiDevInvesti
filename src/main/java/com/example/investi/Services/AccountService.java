package com.example.investi.Services;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.AccountStatus;
import com.example.investi.Entities.AccountType;
import com.example.investi.Entities.user;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Account createAccount(Account account, Long clientId, AccountType accountType) {
        // Fetch the existing Client from the database
        user existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        // Check if the Client already has an account of the specified type
        List<Account> existingAccount = accountRepository.findByClientIdAndAccountType(clientId, accountType);
        System.out.println("account found ");
        if (existingAccount.size()>0) {
            throw new RuntimeException("Client already has a " + accountType + " account.");
        }

        // Associate the existing Client with the Account
        account.setClient(existingClient);

        // Set irrelevant attributes to null based on the account type
        if (accountType == AccountType.Current) {
            account.setTauxInteret(0);
            account.setPlafondRetrait(0);
        } else if (accountType == AccountType.Saving) {
            account.setDecouvertAutorise(0);
            account.setFraisMensuels(0);
        }

        // Set the account type
        account.setAccountType(accountType);

        // Set default status
        account.setStatus(AccountStatus.ACTIF);

        // Save the account
        return accountRepository.save(account);
    }


    public List<Account> listAccounts(Long clientId, Optional<AccountType> accountType) {
        if (accountType.isPresent()) {
            // Fetch accounts of a specific type for the client
            return accountRepository.findByClientIdAndAccountType(clientId, accountType.get());
        } else {
            // Fetch all accounts for the client
            return accountRepository.findByClientIdUser(clientId);
        }
    }

    public Account updateAccount(Long accountId, Account updatedAccount, Long clientId) {
        // Fetch the existing account by ID
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Optional: Verify that the account belongs to the specified client
        if (!existingAccount.getClient().getIdUser().equals(clientId)) {
            throw new RuntimeException("Account does not belong to the specified client.");
        }

        // Update common fields
        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setBalance(updatedAccount.getBalance());

        // Update type-specific fields based on the account type
        if (existingAccount.getAccountType() == AccountType.Current) {
            existingAccount.setDecouvertAutorise(updatedAccount.getDecouvertAutorise());
            existingAccount.setFraisMensuels(updatedAccount.getFraisMensuels());
        } else if (existingAccount.getAccountType() == AccountType.Saving) {
            existingAccount.setTauxInteret(updatedAccount.getTauxInteret());
            existingAccount.setPlafondRetrait(updatedAccount.getPlafondRetrait());
        }

        // Save the updated account
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long accountId, Long clientId) {
        // Fetch the account by ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Optional: Verify that the account belongs to the specified client
        if (!account.getClient().getIdUser().equals(clientId)) {
            throw new RuntimeException("Account does not belong to the specified client.");
        }

        // Delete the account
        accountRepository.delete(account);
    }
}