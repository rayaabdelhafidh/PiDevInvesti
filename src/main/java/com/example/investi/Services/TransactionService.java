package com.example.investi.Services;

import com.example.investi.Entities.*;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.TransactionRepository;
import com.example.investi.TransactionRecorder;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Value("${ganache.contract.address}")
    private String contractAddress;

    @Value("${ganache.sender.privateKey}")
    private String privateKey;

    @Autowired

    private Web3j web3j;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private SmsService smsService;
   ;

    // Méthode pour calculer les frais en fonction du type de compte

    private BigDecimal calculateFeeBasedOnAccountType(BigDecimal amount, AccountType accountType) {
        BigDecimal fee = BigDecimal.ZERO;

        switch (accountType) {
            case Current:
                // Pour les comptes courants : frais fixes + pourcentage
                BigDecimal currentFixedFee = new BigDecimal("5.00"); // Frais fixes de 5 €
                BigDecimal currentPercentageFee = amount.multiply(new BigDecimal("0.01")); // 1 % du montant
                fee = currentFixedFee.add(currentPercentageFee);
                break;

            case Saving:
                // Pour les comptes épargne : frais proportionnels uniquement
                fee = amount.multiply(new BigDecimal("0.02")); // 2 % du montant
                break;

            case Project:
                // Pour les comptes projets : frais fixes uniquement
                fee = new BigDecimal("2.00"); // Frais fixes de 2 €
                break;

            default:
                // Par défaut, aucun frais
                fee = BigDecimal.ZERO;
                break;
        }

        // Arrondir à deux décimales
        return fee.setScale(2, RoundingMode.HALF_UP);
    }



    public Transaction createTransaction(Long senderAccountId, Long receiverAccountId, BigDecimal amount) throws Exception {
        // Fetch the sender's account
        Account senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found with ID: " + senderAccountId));

        // Fetch the receiver's account
        Account receiverAccount = accountRepository.findById(receiverAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found with ID: " + receiverAccountId));

        // Calculate fees based on the sender's account type
        BigDecimal fee = calculateFeeBasedOnAccountType(amount, senderAccount.getAccountType());

        // Validate sufficient balance in the sender's account (including fees)
        BigDecimal totalAmountWithFees = amount.add(fee);
        if (senderAccount.getBalance() - totalAmountWithFees.doubleValue() < 0) {
            throw new RuntimeException("Insufficient balance in the sender's account.");
        }

        // Deduct the amount and fees from the sender's account
        senderAccount.setBalance(senderAccount.getBalance() - totalAmountWithFees.doubleValue());

        // Add the amount to the receiver's account (fees are not transferred to the receiver)
        receiverAccount.setBalance(receiverAccount.getBalance() + amount.doubleValue());

        // Save the updated account balances
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Create the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setFee(fee);

        // Determine the transaction type based on the receiver's account type
        if (receiverAccount.getAccountType() == AccountType.Project) {
            transaction.setType(TransactionType.INVESTMENT); // Set type to Investment for Project accounts
        } else {
            transaction.setType(TransactionType.TRANSFERT); // Default type for other accounts
        }

        // Set the transaction description
        transaction.setDescription(
                "Transfer from account " + senderAccountId + " to account " + receiverAccountId
        );

        // Set the transaction status
        transaction.setStatus(TransactionStatus.VALIDEE);

        // Associate the sender and receiver accounts with the transaction
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);

        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Record the transaction on the blockchain
        recordTransactionOnBlockchain(senderAccount, receiverAccount, amount, fee, transaction.getDescription());

        return savedTransaction;
    }

    private void recordTransactionOnBlockchain(Account sender, Account receiver,
                                               BigDecimal amount, BigDecimal fee,
                                               String description) throws Exception {

        // 1. Load credentials
        Credentials credentials = Credentials.create(privateKey);

        // 2. Load contract
      BigInteger GAS_LIMIT = BigInteger.valueOf(100_000L); // Example: 100,000 gas
        BigInteger GAS_PRICE = BigInteger.valueOf(5_000_000_000L);

        TransactionRecorder contract = TransactionRecorder.load(
                contractAddress,
                web3j,
                credentials,
                new StaticGasProvider(GAS_PRICE, GAS_LIMIT) // Custom gas provider
        );

        // 3. Convert to Wei
        BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        BigInteger feeWei = Convert.toWei(fee, Convert.Unit.ETHER).toBigInteger();

        // 4. Execute contract method
        TransactionReceipt receipt = contract.recordTransaction(
                sender.getEthereumAddress(),
                receiver.getEthereumAddress(),
                amountWei,
                feeWei,
                description
        ).send();

        // 5. Verify success
        if (!receipt.isStatusOK()) {
            throw new RuntimeException("Blockchain transaction failed");
        }
    }

    private void sendTransactionNotifications(Account senderAccount, Account receiverAccount, BigDecimal amount, BigDecimal fee) {
        String senderMessage = "You sent " + amount + " units. Fee: " + fee + ". New balance: " + senderAccount.getBalance();
        String receiverMessage = "You received " + amount + " units. New balance: " + receiverAccount.getBalance();

        smsService.sendSms("+21629176912", senderMessage);
    }
    public BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        // Obtenir le taux de change
        double exchangeRate = currencyService.getExchangeRate(fromCurrency, toCurrency);

        // Convertir le montant
        return amount.multiply(BigDecimal.valueOf(exchangeRate))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public String buildHtmlReport(Client client, List<Account> accounts, LocalDate startDate, LocalDate endDate, String accountNumber) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Add client information
        htmlBuilder.append("<h1>Transaction Report</h1>")
                .append("<h2>Client: ").append(client.getFirstName()).append("</h2>")
                .append("<p>Email: ").append(client.getEmail()).append("</p>");

        // Add account summary
        htmlBuilder.append("<h3>Accounts:</h3><ul>");
        for (Account account : accounts) {
            if (accountNumber != null && !account.getAccountNumber().equals(accountNumber)) continue;
            htmlBuilder.append("<li>")
                    .append("Account Number: ").append(account.getAccountNumber())
                    .append(", Balance: ").append(account.getBalance())
                    .append(", Type: ").append(account.getAccountType())
                    .append("</li>");
        }
        htmlBuilder.append("</ul>");

        // Add transaction details
        htmlBuilder.append("<h3>Transactions:</h3><table border='1'>")
                .append("<tr><th>Date</th><th>Type</th><th>Description</th><th>Amount</th><th>Balance</th></tr>");
        for (Account account : accounts) {
            if (accountNumber != null && !account.getAccountNumber().equals(accountNumber)) continue;
            List<Transaction> transactions = transactionRepository.findByAccountIdAndDates(
                    account.getId(), startDate, endDate);
            for (Transaction transaction : transactions) {
                htmlBuilder.append("<tr>")
                        .append("<td>").append(transaction.getTransactionDate()).append("</td>")
                        .append("<td>").append(transaction.getType()).append("</td>")
                        .append("<td>").append(transaction.getDescription()).append("</td>")
                        .append("<td>").append(transaction.getAmount()).append("</td>")
                        .append("<td>").append(transaction.getBalanceAfterTransaction()).append("</td>")
                        .append("</tr>");
            }
        }
        htmlBuilder.append("</table>");

        return htmlBuilder.toString();
    }

    public byte[] generatePdfFromHtml(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

}