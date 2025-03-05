package com.example.investi.Services;

import com.example.investi.Entities.*;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.ClientRepository;
import com.example.investi.Repositories.ProjectRepository;
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

    @Autowired
    private ProjectRepository projectRepository;

    public Account createAccount(Account account, Long clientId, AccountType accountType, Long project) {
        // Validate that a Project is provided for Project accounts
        if (accountType == AccountType.Project && project == null) {
            throw new RuntimeException("A Project must be provided for a Project account.");
        }

        // Associate the Client only if the account type is not Project
        if (accountType != AccountType.Project) {
            // Fetch the existing Client from the database
            Client existingClient = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

            // Check if the Client already has an account of the specified type
            List<Account> existingAccounts = accountRepository.findByClientIdAndAccountType(clientId, accountType);
            if (!existingAccounts.isEmpty()) {
                throw new RuntimeException("Client already has a " + accountType + " account.");
            }

            // Associate the existing Client with the Account
            account.setClient(existingClient);
        } else {
            // For Project accounts, set the client to null
            account.setClient(null);
        }

        // Handle specific logic based on the account type
        switch (accountType) {
            case Current:
                // Set irrelevant attributes to 0 or null for Current accounts
                account.setDecouvertAutorise(0);
                account.setFraisMensuels(0);

                account.setProject(null); // No project for Current accounts
                break;

            case Saving:
                // Set irrelevant attributes to 0 or null for Saving accounts
                account.setTauxInteret(0);
                account.setPlafondRetrait(0);

                account.setProject(null); // No project for Saving accounts
                break;

            case Project:
                // Save the Project entity


                // Associate the Project with the Account
                account.setProject(projectRepository.findById(project.intValue()).get());

                // Set irrelevant attributes to 0 or null for Project accounts
                account.setFraisMensuels(0);
                account.setTauxInteret(0);
                account.setPlafondRetrait(0);
                account.setDecouvertAutorise(0);
                break;

            default:
                throw new RuntimeException("Invalid account type: " + accountType);
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
            return accountRepository.findByClientId(clientId);
        }
    }

    public Account updateAccount(Long accountId, Account updatedAccount, Long clientId, Long projectId) {
        // Fetch the existing account by ID
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Handle updates based on the account type
        if (existingAccount.getAccountType() == AccountType.Project) {
            // For Project accounts, validate that a projectId is provided
            if (projectId == null) {
                throw new RuntimeException("A Project ID must be provided for updating a Project account.");
            }

            // Fetch the updated Project entity
            Project updatedProject = projectRepository.findById(projectId.intValue())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

            // Associate the updated Project with the Account
            existingAccount.setProject(updatedProject);
        } else {
            // For Current and Saving accounts, verify that the account belongs to the specified client
            if (!existingAccount.getClient().getId().equals(clientId)) {
                throw new RuntimeException("Account does not belong to the specified client.");
            }
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
        } else if (existingAccount.getAccountType() == AccountType.Project) {
            // For Project accounts, ensure the balance is updated
            existingAccount.setBalance(updatedAccount.getBalance());
        }

        // Save the updated account
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long accountId, Long clientId) {
        // Fetch the account by ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Optional: Verify that the account belongs to the specified client
        if (!account.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Account does not belong to the specified client.");
        }

        // Delete the account
        accountRepository.delete(account);
    }
}