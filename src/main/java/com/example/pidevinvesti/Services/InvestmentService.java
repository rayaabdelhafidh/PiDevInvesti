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
import java.util.*;

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
            List<Transaction> transactions = transactionRepository.findByType(TransactionType.INVESTMENT);

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
                    ReturnInvestment(investment.getInvestId());
                }
            }
        }
    }

    @Override
    public void ReturnInvestment(Integer investment_id) {
        Investment investment = investmentRepository.findById(investment_id)
                .orElseThrow(() -> new IllegalStateException("Investment not found"));

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

        // ✅ Calculate return on investment
        BigDecimal totalReturn = project.getTotalReturn();
        BigDecimal investmentAmount = investment.getAmount();

        // Ensure valid share calculation
        BigDecimal share = BigDecimal.ZERO;
        if (project.getCumulInvest().compareTo(BigDecimal.ZERO) > 0) {
            share = investmentAmount.divide(project.getCumulInvest(), 10, RoundingMode.HALF_UP);
        }

        BigDecimal investmentReturnAmount = totalReturn.multiply(share);

        // ✅ Ensure ROI is correctly calculated and not negative
        BigDecimal roi = BigDecimal.ZERO;
        if (investmentAmount.compareTo(BigDecimal.ZERO) > 0) {
            roi = (investmentReturnAmount.subtract(investmentAmount))
                    .divide(investmentAmount, 10, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            // If ROI is negative, set it to 0 (to avoid incorrect calculations)
            if (roi.compareTo(BigDecimal.ZERO) < 0) {
                roi = BigDecimal.ZERO;
                investmentReturnAmount = investmentAmount; // Investor gets at least their initial amount
            }
        }

        // ✅ Ensure the project account has enough funds before transferring
        if (projectAccount.getBalance() == 0 || projectAccount.getBalance() < investmentReturnAmount.doubleValue()) {
            throw new IllegalStateException("Insufficient funds in project account to return investment.");
        }

        // ✅ Register the investment return
        InvestmentReturn investmentReturn = new InvestmentReturn();
        investmentReturn.setInvestment(investment);
        investmentReturn.setRoiPercentage(roi);
        investmentReturn.setTotalReturn(investmentReturnAmount);
        investmentReturn.setPayoutDate(new Date());
        investmentReturn.setInvestor(investor);
        investmentReturnRepository.save(investmentReturn);

        // ✅ Process the transaction
        Transaction returnTransaction = transactionService.createTransaction(
                investorAccount.getId(),
                projectAccount.getId(),
                investmentReturnAmount
        );

        transactionService.addTransaction(returnTransaction);
        transactionRepository.save(returnTransaction);

        // ✅ Update account balances (subtract from project, add to investor)
        projectAccount.setBalance(projectAccount.getBalance() - investmentReturnAmount.doubleValue());
        investorAccount.setBalance(investorAccount.getBalance() + investmentReturnAmount.doubleValue());

        accountRepository.save(projectAccount);
        accountRepository.save(investorAccount);
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
        investment.setDescriptionInvest(project.getDescriptionProject());
        investment.setAmount(investmentAmount);
        investment.setStatusInvest(StatusInvest.ACCEPTED);
        investment.setInvestmentProgress(ProgressInvestment.IN_PROGRESS);
        investment.setInvestmentDate(new Date());

        // Add investment to project list and update cumulative investment
        project.getInvestments().add(investment); // Ensures consistency
        project.setCumulInvest(newTotalInvestment);
        projectRepository.save(project);
        // Save the investment

        // Create Transactions
        Transaction feeTransaction = transactionService.createTransaction(investorAccount.getId(), projectAccount.getId(), adminFee);
        Transaction investmentTransaction = transactionService.createTransaction(investorAccount.getId(), projectAccount.getId(), investmentAmount);
        transactionService.addTransaction(investmentTransaction);
        transactionService.addTransaction(feeTransaction);
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
        return investment;
    }


    @Override
    public List<Investment> findByStatus(StatusInvest status) {
        return investmentRepository.findByStatusInvest(status);
    }

    public List<Map<String, Object>> getInvestmentData() {
        List<Investment> investments = investmentRepository.findAllInvestmentsWithProjects();
        List<Map<String, Object>> data = new ArrayList<>();

        for (Investment i : investments) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("amount", i.getAmount());
            entry.put("investmentDate", i.getInvestmentDate());
            entry.put("projectStatus", i.getProject().getProjectStatus());
            entry.put("amountNeeded", i.getProject().getAmountNeeded());
            entry.put("cumulInvest", i.getProject().getCumulInvest());
            entry.put("totalReturn", i.getProject().getTotalReturn());
            entry.put("investmentProgress", i.getInvestmentProgress()); // Cible
            data.add(entry);
        }
        return data;
    }

}
