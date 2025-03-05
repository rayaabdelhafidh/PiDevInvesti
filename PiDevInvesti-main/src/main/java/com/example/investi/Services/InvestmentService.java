package com.example.investi.Services;

import com.example.investi.Entities.*;
import com.example.investi.Repositories.*;
import com.example.investi.config.EmailService;
import com.example.investi.config.Mail;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class InvestmentService implements IInvestmentService<Investment, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(InvestmentService.class);

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
     @Autowired
     private EmailService emailService;
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
        investment.setInvestmentProgress(ProgressInvestment.IN_PROGRESS);
        return investmentRepository.save(investment);
    }
    @Override
    public Investment RefuseInvestment(Integer invest_id){
        Investment investment=investmentRepository.findById(invest_id).orElse(null);
        investment.setStatusInvest(StatusInvest.REFUSED);
        investment.setInvestmentProgress(ProgressInvestment.FAILED);
        return investmentRepository.save(investment);
    }
    @Override
    public void Checkinvest() {
        List<Investment> investments = investmentRepository.findByStatusInvest(StatusInvest.ACCEPTED);


        for (Investment investment : investments) {
            Project project = investment.getProject();
            if (project != null) {
                // Calculate the next scheduled date for ROI calculation
                Date nextScheduledDate = calculateNextScheduledDate(investment, project);

                // If the next scheduled date is today, process the investment
                if (nextScheduledDate != null && LocalDate.now().isEqual(nextScheduledDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                    List<Transaction> transactions = transactionRepository.findByType(TransactionType.INVESTMENT);

                    for (Transaction transaction : transactions) {
                        LocalDate transactionDate = transaction.getDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        LocalDate nextDueDate = transactionDate.plusMonths(1);

                        // Check if the transaction is due
                        if (LocalDate.now().isAfter(nextDueDate) && transaction.getStatus() == TransactionStatus.EN_ATTENTE) {
                            // Update the transaction date and status
                            Date updatedDate = Date.from(nextDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            transaction.setDate(updatedDate);
                            transaction.setStatus(TransactionStatus.VALIDEE);

                            // Save updated transaction and process return
                            transactionRepository.save(transaction);
                            ReturnInvestment(investment.getInvestId());
                        }
                    }
                }
            }
        }
    }

    public String getNextReturnMessage(Investment investment, Project project) {
        Date nextReturnDate = calculateNextScheduledDate(investment, project);

        if (nextReturnDate == null) {
            return "Investment return will not be generated in the near future.";
        }

        // Format the next return date into a readable format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(nextReturnDate);

        return "The next return for this investment will be generated on " + formattedDate + ".";
    }
    // Method to calculate next scheduled date for ROI calculation
    public Date calculateNextScheduledDate(Investment investment, Project project) {
        Calendar calendar = Calendar.getInstance();
        Date baseDate = investment.getInvestmentDate() != null ? investment.getInvestmentDate() : project.getStartDate();

        calendar.setTime(baseDate);

        // For quarterly ROI calculation
        int durationInMonths = project.getProjectDuration();
        int monthsBetween = (int) ((System.currentTimeMillis() - baseDate.getTime()) / (1000 * 60 * 60 * 24 * 30));

        if (monthsBetween % 3 == 0 && monthsBetween <= durationInMonths) {
            calendar.add(Calendar.MONTH, 3);
        } else {
            return null; // Not time yet
        }

        return calendar.getTime();
    }

    @Scheduled(cron = "0 0 0 1 */3 ?") // Executes at midnight on the first day of every third month
    public void scheduledReturnInvestment() {
        // Fetch all investments
        List<Investment> investments = investmentRepository.findAll();
        for (Investment investment : investments) {
            // Check if it's time for the return calculation
            ReturnInvestment(investment.getInvestId());
        }
    }
    public void triggerScheduledReturnInvestmentManually() {
        List<Investment> investments = investmentRepository.findAll(); // Fetch all investments or a subset
        for (Investment investment : investments) {
            ReturnInvestment(investment.getInvestId());
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

        // Generate message for when the next return will occur
        String returnMessage = getNextReturnMessage(investment, project);
        logger.info(returnMessage);  // Log the message so it’s visible in the logs

        // Retrieve investor's account
        Investor investor = investment.getInvestor();
        Account investorAccount = accountRepository.findByInvestor(investor);
        Account projectAccount = accountRepository.findByProject(project);

        if (investorAccount == null || projectAccount == null) {
            throw new IllegalStateException("Investor or project account is missing.");
        }

        // Calculate return on investment
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
                projectAccount.getId(),
                investorAccount.getId(),
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
    public Investment Invest(long owner_id, BigDecimal amount_invested, Integer project_id)throws MessagingException {
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
        Mail mail = new Mail();
        mail.setTo(investorAccount.getInvestor().getEmail());
        mail.setSubject("Investment Confirmation");
        mail.setContent("Dear " + investorAccount.getInvestor().getFirstName() + ",\n\n"
                + "Your investment of " + amount_invested + " has been successfully processed.\n"
                + "You will receive your ROI on a quarterly basis.\n\n"
                + "Best regards,\nInvestment Team");

        emailService.sendHtmlEmail(mail);

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
