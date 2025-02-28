package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.*;
import com.example.pidevinvesti.Repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
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
        investment.setStatusInvest(StatusInvest.UNDER_REVIEW);
        return investmentRepository.save(investment);
    }
    @Override
    public Investment RefuseInvestment(Integer invest_id){
        Investment investment=investmentRepository.findById(invest_id).orElse(null);
        investment.setStatusInvest(StatusInvest.REFUSED);
        return investmentRepository.save(investment);
    }

    @Override
    public void Checkinvest() throws MappingException {
        List<Investment> investments = investmentRepository.findByStatusInvest(StatusInvest.ACCEPTED);

        for (Investment investment : investments) {
            List<Transaction> transactions = investment.getTransactions();

            for (Transaction transaction : transactions) {
                if (transaction.getTransactionType() == TransactionType.INVESTMENT) {
                    // Convertir Date en LocalDate
                    LocalDate transactionDate = transaction.getDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Ajouter un mois
                    LocalDate nextDueDate = transactionDate.plusMonths(1);

                    // Vérifier si la date actuelle est après la date due
                    if (LocalDate.now().isAfter(nextDueDate)) {
                        // Mettre à jour la date de la transaction (reconvertir en Date)
                        Date updatedDate = Date.from(nextDueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        transaction.setDate(updatedDate);

                        // Sauvegarde et appel de ReturnInvestment
                        transactionRepository.save(transaction);
                        ReturnInvestment(transaction);
                    }
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

        // Calcul du retour sur investissement
        BigDecimal totalReturn = project.getTotalReturn(); // Revenus générés par le projet
        BigDecimal share = investment.getAmount().divide(project.getCumulInvest(), RoundingMode.HALF_UP);
        BigDecimal investmentReturnAmount = totalReturn.multiply(share);

        // Calcul du ROI en pourcentage
        BigDecimal roi = investmentReturnAmount.subtract(investment.getAmount())
                .divide(investment.getAmount(), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)); // ROI en %

        // Enregistrer le retour
        InvestmentReturn investmentReturn = new InvestmentReturn();
        investmentReturn.setInvestment(investment);
        investmentReturn.setRoiPercentage(roi); // ROI en %
        investmentReturn.setTotalReturn(investmentReturnAmount);
        investmentReturn.setPayoutDate(Date.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Sauvegarde
        investmentReturnRepository.save(investmentReturn);
    }


    @Override
    public Investment Invest(int owner_id, BigDecimal amount_invested, Integer investment_id,Integer project_id) {
        Investment investment = investmentRepository.findById(investment_id).orElse(null);
        Project project=projectRepository.findById(project_id).orElse(null);
        if (investment == null ) {
            throw new IllegalArgumentException("Investment not found.");
        }
        if (project == null ) {
            throw new IllegalArgumentException("Project not found.");
        }

        long account_id = accountRepository.findByClientId(owner_id);
        Account account_sender = accountRepository.findById(account_id).orElse(null);
        Account account_receiver = accountRepository.findByProject(project);
        Account account_admin = accountRepository.findById(1L).orElse(null); // Admin account for fees

        if (account_sender == null || account_receiver == null || account_admin == null) {
            throw new IllegalArgumentException("One or more accounts involved in the transaction are missing.");
        }

        // Validate investment amount
        BigDecimal newTotalInvestment = investment.getAmount().add(amount_invested);
        if (newTotalInvestment.compareTo(project.getAmountNeeded()) > 0) {
            throw new IllegalArgumentException("The amount exceeds the required investment. Please try a smaller amount.");
        }

        if (newTotalInvestment.compareTo(project.getAmountNeeded()) == 0) {
            investment.setInvestmentProgress(ProgressInvestment.COMPLETED);
        }

        // Calculate amounts
        BigDecimal income = amount_invested.multiply(new BigDecimal("0.05")); // 5% commission
        BigDecimal inv_amount = amount_invested.multiply(new BigDecimal("0.95")); // 95% investment

        // Update investment amount
        investment.setAmount(newTotalInvestment);

        // Create & save transaction for admin fee (income)
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(income);
        transaction1.setSenderAccount(account_sender);
        transaction1.setReceiverAccount(account_admin);
        transaction1.setInvestment(investment);
        transaction1.setTransactionType(TransactionType.INVESTMENT);
        transaction1.setDate(new Date()); // Use `Date` instead of `LocalDate`
        transaction1 = transactionService.addTransaction(transaction1);

        // Create & save transaction for investment
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(inv_amount);
        transaction2.setSenderAccount(account_sender);
        transaction2.setReceiverAccount(account_receiver);
        transaction2.setTransactionType(TransactionType.INVESTMENT);
        transaction2.setInvestment(investment);
        transaction2.setDate(new Date()); // Use `Date` instead of `LocalDate`
        transaction2 = transactionService.addTransaction(transaction2);

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


}
