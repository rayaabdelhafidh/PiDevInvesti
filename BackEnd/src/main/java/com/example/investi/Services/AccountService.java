package com.example.investi.Services;

import com.example.investi.Entities.*;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.ClientRepository;
import com.example.investi.Repositories.ProjectRepository;
import com.example.investi.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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

    @Autowired
    CarteBancaireService carteBancaireService;

    public Account createAccount(Account account, Long clientId, AccountType accountType, Long project) {

        ECKeyPair keyPair = null;
        try {
            keyPair = Keys.createEcKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        String address = "0x" + Keys.getAddress(keyPair);
        if (accountType == AccountType.Project && project == null) {
            throw new RuntimeException("A Project must be provided for a Project account.");
        }

        if (accountType != AccountType.Project) {
            Client existingClient = clientRepository.findClientById(clientId);
            if(existingClient == null)
            {

                throw new RuntimeException("cClient not found");
            }


            List<Account> existingAccounts = accountRepository.findByClientIdAndAccountType(clientId, accountType);
            if (!existingAccounts.isEmpty()) {
                throw new RuntimeException("Client already has a " + accountType + " account.");
            }

            account.setClient(existingClient);
        } else {
            account.setClient(null);
        }

        switch (accountType) {
            case Current:
                account.setTauxInteret(0);
                account.setPlafondRetrait(0);
                account.setEthereumAddress(address);
                account.setProject(null);
                break;

            case Saving:
                account.setDecouvertAutorise(0);
                account.setFraisMensuels(0);
                account.setEthereumAddress(address);

                account.setProject(null);
                break;

            case Project:



                account.setProject(projectRepository.findById(project.intValue()).get());

                account.setFraisMensuels(0);
                account.setTauxInteret(0);
                account.setPlafondRetrait(0);
                account.setDecouvertAutorise(0);
                break;

            default:
                throw new RuntimeException("Invalid account type: " + accountType);
        }

        account.setAccountType(accountType);

        account.setStatus(AccountStatus.ACTIF);

        return accountRepository.save(account);
    }


    public List<Account> listAccounts(Long clientId, Optional<AccountType> accountType) {
        if (accountType.isPresent()) {
            return accountRepository.findByClientIdAndAccountType(clientId, accountType.get());
        } else {
            return accountRepository.findByClientId(clientId);
        }
    }



    public void deleteAccount(Long accountId, Long clientId) {
        // Récupérer le compte par son ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé"));

        // Vérifier si le clientId est fourni et correspond au propriétaire du compte
        if (clientId != null && !account.getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Le client spécifié ne possède pas ce compte.");
        }

        // Supprimer la carte bancaire associée (si elle existe)
        CarteBancaire carteBancaire = account.getCarteBancaire();
        if (carteBancaire != null) {
            carteBancaireService.deleteCarteBancaire(carteBancaire.getId());
        }

        // Supprimer le compte
        accountRepository.delete(account);
    }

    public List<Account> listAllAccounts(Optional<AccountType> type) {
        if (type.isPresent()) {
            // Fetch accounts filtered by the specified account type
            return accountRepository.findByAccountType(type.get());
        } else {
            // Fetch all accounts if no type is specified
            return accountRepository.findAll();
        }
    }

    public Account updateAccountForAdmin(Long accountId, Account updatedAccount, Long projectId) {
        // Fetch the existing account by ID
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        // Validate Project ID for Project accounts
        if (existingAccount.getAccountType() == AccountType.Project) {
            if (projectId == null) {
                throw new RuntimeException("A Project ID must be provided for updating a Project account.");
            }

            Project updatedProject = projectRepository.findById(projectId.intValue())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

            existingAccount.setProject(updatedProject);
        }

        // Update common fields
        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setBalance(updatedAccount.getBalance());

        // Update type-specific fields
        if (existingAccount.getAccountType() == AccountType.Current) {
            existingAccount.setDecouvertAutorise(updatedAccount.getDecouvertAutorise());
            existingAccount.setFraisMensuels(updatedAccount.getFraisMensuels());
        } else if (existingAccount.getAccountType() == AccountType.Saving) {
            existingAccount.setTauxInteret(updatedAccount.getTauxInteret());
            existingAccount.setPlafondRetrait(updatedAccount.getPlafondRetrait());
        } else if (existingAccount.getAccountType() == AccountType.Project) {
            existingAccount.setBalance(updatedAccount.getBalance());
        }

        // Save the updated account
        return accountRepository.save(existingAccount);
    }

    public Account updateAccount(Long accountId, Account updatedAccount, Long clientId, Long projectId) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        if (existingAccount.getAccountType() == AccountType.Project) {
            if (projectId == null) {
                throw new RuntimeException("A Project ID must be provided for updating a Project account.");
            }

            Project updatedProject = projectRepository.findById(projectId.intValue())
                    .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

            existingAccount.setProject(updatedProject);
        } else {
            if (!existingAccount.getClient().getId().equals(clientId)) {
                throw new RuntimeException("Account does not belong to the specified client.");
            }
        }

        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setBalance(updatedAccount.getBalance());

        if (existingAccount.getAccountType() == AccountType.Current) {
            existingAccount.setDecouvertAutorise(updatedAccount.getDecouvertAutorise());
            existingAccount.setFraisMensuels(updatedAccount.getFraisMensuels());
        } else if (existingAccount.getAccountType() == AccountType.Saving) {
            existingAccount.setTauxInteret(updatedAccount.getTauxInteret());
            existingAccount.setPlafondRetrait(updatedAccount.getPlafondRetrait());
        } else if (existingAccount.getAccountType() == AccountType.Project) {
            existingAccount.setBalance(updatedAccount.getBalance());
        }

        // Save the updated account
        return accountRepository.save(existingAccount);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));
    }
}