package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.*;
import com.example.pidevinvesti.Repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class InvestmentService implements IInvestmentService<Investment, Integer> {

    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private InvestmentReturnRepository investmentReturnRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private InvestorRepository investorRepository;
    @Autowired
    private TransactionService transactionService;

    @Override
    public Investment save(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Investment add(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Optional<Investment> findById(Integer id) {
        return investmentRepository.findById(id);
    }

    @Override
    public Investment update(Integer id, Investment newInvestment) {
        return investmentRepository.findById(id)
                .map(existingInvestment -> {
                    existingInvestment.setAmount(newInvestment.getAmount());
                    existingInvestment.setInvestmentDate(newInvestment.getInvestmentDate());
                    existingInvestment.setDescriptionInvest(newInvestment.getDescriptionInvest());
                    existingInvestment.setStatusInvest(newInvestment.getStatusInvest());
                    return investmentRepository.save(existingInvestment);
                }).orElse(null);
    }

    @Override
    public List<Investment> findAll() {
        return investmentRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        investmentRepository.deleteById(id);
    }

    @Override
    public void delete(Investment investment) {
        investmentRepository.delete(investment);
    }

    @Override
    public Investment AcceptInvestment(Integer invest_id){
        Investment investment=investmentRepository.findById(invest_id).orElse(null);
        investment.setStatusInvest(StatusInvest.ACCEPTED);
        //investment.setStatusInvest(StatusInvest.UNDER_REVIEW);
        return investmentRepository.save(investment);
    }
    @Override
    public Investment RefuseInvestment(Integer invest_id){
        Investment investment=investmentRepository.findById(invest_id).orElse(null);
        investment.setStatusInvest(StatusInvest.REFUSED);
        return investmentRepository.save(investment);
    }
    @Override
    public void Checkinvest() {
        List<Investment> investments = investmentRepository.findByStatusInvest(StatusInvest.ACCEPTED);

        for (Investment investment : investments) {
            // Fetch only transactions of type INVESTMENT
            List<Transaction> transactions = transactionRepository.findByInvestmentAndType(investment, TransactionType.INVESTMENT);

            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                LocalDate nextDueDate = transactionDate.plusMonths(1);

                if (LocalDate.now().isAfter(nextDueDate) && transaction.getStatus() == TransactionStatus.EN_ATTENTE) {
                    // Update date
                    Date updatedDate = Date.from(nextDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    transaction.setDate(updatedDate);
                    transaction.setStatus(TransactionStatus.VALIDEE); // Avoid reprocessing

                    // Save and process return
                    transactionRepository.save(transaction);
                    ReturnInvestment(transaction);
                }
            }
        }
    }

    @Override
    public void ReturnInvestment(Transaction transaction) {
        Investment investment = transaction.getInvestment();
        Project project = investment.getProject();

        if (project == null || project.getCumulInvest().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Le projet ou l'investissement est invalide");
        }

        // Retrieve investor's account
        Investor investor = investment.getInvestor();
        Account investorAccount = accountRepository.findByInvestor(investor);
        Account projectAccount = accountRepository.findByProject(project);

        if (investorAccount == null || projectAccount == null) {
            throw new IllegalStateException("Investor or project account is missing.");
        }

        // Calculate return on investment
        BigDecimal totalReturn = project.getTotalReturn();
        BigDecimal share = investment.getAmount().divide(project.getCumulInvest(), RoundingMode.HALF_UP);
        BigDecimal investmentReturnAmount = totalReturn.multiply(share);

        // Calculate ROI percentage
        BigDecimal roi = investmentReturnAmount.subtract(investment.getAmount())
                .divide(investment.getAmount(), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        // Register the investment return
        InvestmentReturn investmentReturn = new InvestmentReturn();
        investmentReturn.setInvestment(investment);
        investmentReturn.setRoiPercentage(roi);
        investmentReturn.setTotalReturn(investmentReturnAmount);
        investmentReturn.setPayoutDate(new Date());

        // Save the investment return
        investmentReturnRepository.save(investmentReturn);

        // Process the transaction (credit investor)
        Transaction returnTransaction = new Transaction();
        returnTransaction.setAmount(investmentReturnAmount);
        returnTransaction.setSenderAccount(projectAccount);  // Project paying back
        returnTransaction.setReceiverAccount(investorAccount);  // Investor receiving
        returnTransaction.setTransactionType(TransactionType.RETURN_ON_INVESTMENT);
        returnTransaction.setInvestment(investment);
        returnTransaction.setDate(new Date());

        transactionRepository.save(returnTransaction);
    }

    @Override
    public Investment Invest(long owner_id, BigDecimal amount_invested, Integer project_id) {
// Find the investor and project
        Investor investor = investorRepository.findById(owner_id)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Check if the investor has an account
        Account investorAccount = accountRepository.findByInvestor(investor);
        Account projectAccount = accountRepository.findByProject(project);

        if (investorAccount == null || projectAccount == null ) {
            throw new RuntimeException("Missing investor or project account.");
        }

        // Check if the investor has enough balance
        if (investorAccount.getBalance() < 0) {
            throw new RuntimeException("Insufficient funds.");
        }

        // Check if the investment doesn't exceed project needs
        BigDecimal newTotalInvestment = project.getCumulInvest().add(amount_invested);
        if (newTotalInvestment.compareTo(project.getAmountNeeded()) > 0) {
            throw new RuntimeException("Investment exceeds project funding needs.");
        }

        // Deduct a 5% fee for the platform
        BigDecimal adminFee = amount_invested.multiply(new BigDecimal("0.05"));
        BigDecimal investmentAmount = amount_invested.subtract(adminFee);

        // Create Investment and link it to the investor & project
        Investment investment = new Investment();
        investment.setInvestor(investor);
        investment.setProject(project);
        investment.setAmount(investmentAmount);

        // Add investment to project list and update cumulative investment
        project.getInvestments().add(investment); // Ensures consistency
        project.setCumulInvest(newTotalInvestment);
        projectRepository.save(project);
        // Save the investment
        investment = investmentRepository.save(investment);

        // Create Transactions
        Transaction feeTransaction = transactionService.createTransaction(investorAccount.getId(), projectAccount.getId(), adminFee);
        Transaction investmentTransaction = transactionService.createTransaction(investorAccount.getId(), projectAccount.getId(), investmentAmount);

        // Link transactions to investment
        investment.getTransactions().add(feeTransaction);
        investment.getTransactions().add(investmentTransaction);
        // Send confirmation email
        /*Mail mail = new Mail();
        mail.setTo(account_sender.getUser().getEmail());
        mail.setSubject("Investment Confirmation");
        mail.setContent("Dear " + account_sender.getUser().getUsername() + ",\n\n"
                + "Your investment of " + amount_invested + " has been successfully processed.\n"
                + "You will receive your ROI on a monthly basis.\n\n"
                + "Best regards,\nInvestment Team");

        emailService.sendHtmlEmail(mail);
*/
        return investmentRepository.save(investment);
    }



    @Override
    public Investment affetcterTransactionToInvestment(List<Long> idTransaction, Integer idInvestment) {
        Investment investment = investmentRepository.findById(idInvestment)
                    .orElseThrow(() -> new RuntimeException("Investment not found"));

            List<Transaction> transactions = transactionRepository.findAllById(idTransaction);

            // Ensure each transaction is linked to the investment
            for (Transaction transaction : transactions) {
                transaction.setInvestment(investment);
            }

            // Save updated transactions
            transactionRepository.saveAll(transactions);

            // Update investment with new transactions
            investment.setTransactions(transactions);
            return investmentRepository.save(investment);
        }

    @Override
    public Investment desaffetcterTransactionFromInvestment(Integer idInvestment) {
        Investment investment=investmentRepository.findById(idInvestment).orElse(null);
        investment.setTransactions(null);
        investmentRepository.save(investment);
        return investment;
    }

}
