package com.example.investi.Services;

import com.example.investi.Entities.*;
import com.example.investi.Repositories.*;
import com.example.investi.Configuration.Mail;
import com.example.investi.Configuration.SMSService;
import jakarta.transaction.Transactional;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class InvestmentService implements IInvestmentService<Investment, Integer> {

    private Logger logger = LoggerFactory.getLogger(InvestmentService.class);

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
    // @Autowired
     //private EmailService emailService;
     @Autowired
     private SMSService smsService;

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
                    existingInvestment.setInvestmentProgress(newInvestment.getInvestmentProgress());

                    return investmentRepository.save(existingInvestment);
                }).orElse(null);
    }

    @Override
    public List<Investment> findAll() {
        return investmentRepository.findAll();
    }

    @Override
    public Investment findByProject(int idProject) {
        return investmentRepository.findByProject_ProjectId(idProject);
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
    @Transactional
    public Map<String, Object> Checkinvest() {
        List<Investment> acceptedInvestments = investmentRepository.findByStatusInvest(StatusInvest.ACCEPTED);
        log.info("Found {} accepted investments to process.", acceptedInvestments.size());

        List<Map<String, Object>> investmentDetails = new ArrayList<>(); // List of maps instead of InvestmentStatus

        boolean allSuccess = true;

        for (Investment investment : acceptedInvestments) {
            Map<String, Object> investmentInfo = processInvestmentAndGetInfo(investment);
            investmentDetails.add(investmentInfo);
            if (!(boolean) investmentInfo.get("success")) {
                allSuccess = false;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("investments", investmentDetails); // Add the list of maps

        if (allSuccess) {
            response.put("message", "Due investments processed successfully.");
            response.put("status", "success");
        } else {
            response.put("message", "Due investments processed with some failures.");
            response.put("status", "partial_failure");
        }

        return response;
    }

    private Map<String, Object> processInvestmentAndGetInfo(Investment investment) {
        Map<String, Object> investmentInfo = new HashMap<>();

        Project project = investment.getProject();
        if (project == null) {
            log.warn("Investment ID {} has no associated project.", investment.getInvestId());
            investmentInfo.put("investmentId", investment.getInvestId());
            investmentInfo.put("amount", investment.getAmount());
            investmentInfo.put("success", false);
            investmentInfo.put("message", "No associated project.");
            return investmentInfo;
        }

        LocalDate nextScheduledDate = calculateNextScheduledDate(investment, project).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        log.info("Processing investment ID: {}, Project: {}, Next scheduled date: {}",
                investment.getInvestId(), project.getProjectId(), nextScheduledDate);

        if (LocalDate.now().isEqual(nextScheduledDate)) {
            try {
                processDueTransactions(investment);
                investmentInfo.put("investmentId", investment.getInvestId());
                investmentInfo.put("amount", investment.getAmount());
                investmentInfo.put("success", true);
                investmentInfo.put("message", "Processed successfully.");
            } catch (Exception e) {
                log.error("Error processing investment ID {}: {}", investment.getInvestId(), e.getMessage());
                investmentInfo.put("investmentId", investment.getInvestId());
                investmentInfo.put("amount", investment.getAmount());
                investmentInfo.put("success", false);
                investmentInfo.put("message", "Error: " + e.getMessage());
            }
        } else {
            log.info("Investment ID: {} is not due yet. Next scheduled date: {}",
                    investment.getInvestId(), nextScheduledDate);
            investmentInfo.put("investmentId", investment.getInvestId());
            investmentInfo.put("amount", investment.getAmount());
            investmentInfo.put("success", false);
            investmentInfo.put("message", "Not due yet.");
        }
        return investmentInfo;
    }


    private void processInvestment(Investment investment) {
        Project project = investment.getProject();
        if (project == null) {
            log.warn("Investment ID {} has no associated project.", investment.getInvestId());
            return;
        }

        LocalDate nextScheduledDate = calculateNextScheduledDate(investment, project).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        log.info("Processing investment ID: {}, Project: {}, Next scheduled date: {}",
                investment.getInvestId(), project.getProjectId(), nextScheduledDate);

        if (LocalDate.now().isEqual(nextScheduledDate)) {
            processDueTransactions(investment);
        } else {
            log.info("Investment ID: {} is not due yet. Next scheduled date: {}",
                    investment.getInvestId(), nextScheduledDate);
        }
    }

    private void processDueTransactions(Investment investment) {
        Project project = investment.getProject();
        Investor investor = investment.getInvestor();

        if (project == null || investor == null) {
            log.warn("Investment ID {} has missing project or investor information.", investment.getInvestId());
            return;
        }

        List<Transaction> allInvestmentTransactions = transactionRepository.findByType(TransactionType.INVESTMENT);
        List<Transaction> relevantTransactions = new ArrayList<>();

        List<Long> accountIds = allInvestmentTransactions.stream()
                .flatMap(transaction -> Stream.of(transaction.getSenderAccount().getId(), transaction.getTargetAccount().getId()))
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Account> accounts = accountRepository.findAllById(accountIds).stream()
                .collect(Collectors.toMap(Account::getId, Function.identity()));

        for (Transaction transaction : allInvestmentTransactions) {
            Account senderAccount = accounts.get(transaction.getSenderAccount());
            Account receiverAccount = accounts.get(transaction.getTargetAccount());

            if (senderAccount != null && receiverAccount != null) {
                if ((senderAccount.getInvestor() != null && senderAccount.getInvestor().equals(investor)) ||
                        (receiverAccount.getInvestor() != null && receiverAccount.getInvestor().equals(investor)) ||
                        (senderAccount.getProject() != null && senderAccount.getProject().equals(project)) ||
                        (receiverAccount.getProject() != null && receiverAccount.getProject().equals(project))) {
                    relevantTransactions.add(transaction);
                }
            }
        }

        log.info("Found {} relevant investment transactions for investment ID {}.", relevantTransactions.size(), investment.getInvestId());

        for (Transaction transaction : relevantTransactions) {
            LocalDate transactionDate = transaction.getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate nextDueDate = transactionDate.plusMonths(1);

            log.info("Checking transaction ID: {}, Date: {}, Next Due Date: {}, Status: {}",
                    transaction.getTransactionId(), transactionDate, nextDueDate, transaction.getStatus());

            if (LocalDate.now().isAfter(nextDueDate) && transaction.getStatus() == TransactionStatus.EN_ATTENTE) {
                updateTransactionAndProcessReturn(transaction, investment);
            } else {
                log.info("Transaction ID: {} is not due or not in EN_ATTENTE status.", transaction.getTransactionId());
            }
        }
    }

    private void updateTransactionAndProcessReturn(Transaction transaction, Investment investment) {
        LocalDate nextDueDate = transaction.getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1);
        Date updatedDate = Date.from(nextDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        transaction.setTransactionDate(updatedDate);
        transaction.setStatus(TransactionStatus.VALIDEE);
        transactionRepository.save(transaction);
        log.info("Updated transaction ID: {}", transaction.getTransactionId());

        log.info("Calling ReturnInvestment for investment ID: {}", investment.getInvestId());
        ReturnInvestment(investment.getInvestId());
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
        Transaction returnTransaction = null;
        try {
            returnTransaction = transactionService.createTransaction(
                    projectAccount.getId(),
                    investorAccount.getId(),
                    investmentReturnAmount
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        transactionService.addTransaction(returnTransaction);
        transactionRepository.save(returnTransaction);

        // ✅ Update account balances (subtract from project, add to investor)
        projectAccount.setBalance(projectAccount.getBalance() - investmentReturnAmount.doubleValue());
        investorAccount.setBalance(investorAccount.getBalance() + investmentReturnAmount.doubleValue());

        accountRepository.save(projectAccount);
        accountRepository.save(investorAccount);
    }

    @Override
    public Investment Invest(long owner_id, BigDecimal amount_invested, Integer project_id)/*throws MessagingException */{
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
        BigDecimal cumulInvest = project.getCumulInvest() != null ? project.getCumulInvest() : BigDecimal.ZERO;
        BigDecimal amountNeeded = project.getAmountNeeded() != null ? project.getAmountNeeded() : BigDecimal.ZERO;

        BigDecimal newTotalInvestment = cumulInvest.add(amount_invested);
        if (newTotalInvestment.compareTo(amountNeeded) > 0) {
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
        Transaction feeTransaction = null;
        try {
            feeTransaction = transactionService.createTransaction(investorAccount.getId(), projectAccount.getId(), adminFee);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Transaction investmentTransaction = null;
        try {
            investmentTransaction = transactionService.createTransaction(investorAccount.getId(), projectAccount.getId(), investmentAmount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

       // emailService.sendHtmlEmail(mail);

        smsService.sendSMS(String.valueOf(92062284),"Welcome to Investi \n\r"
              .concat("Mrs,Mr : "
                     .concat(investorAccount.getInvestor().getFirstName()
                             .concat(" "+investorAccount.getInvestor().getLastName())
                                .concat("\"we inform you that your investment has been Accepted and proceeded.\n\r "
                                        .concat("\n\r")
                                       .concat("Your investment amount is "+amount_invested+"\n\r You will receive your ROI on a quarterly basis.\n\r Best regards,\nInvestment Team")
                                    ))));

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
