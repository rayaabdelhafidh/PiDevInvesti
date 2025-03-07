package com.example.investi.config;
/*
import com.example.investi.Entities.Investment;
import com.example.investi.Entities.Investor;
import com.example.investi.Services.InvestmentService;
import com.example.investi.Services.InvestorService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class EmailScheduler {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private InvestorService investorService;
    @Autowired
    private InvestmentService investmentService;

    // Envoi d'un email HTML à un investisseur
    public void sendHtmlEmail(String investorMail, String investorName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreply@investi.com");
        helper.setTo(investorMail);
        helper.setSubject("Rappel : ROI pour " + investorName);

        String htmlContent = "<!DOCTYPE html><html lang=\"fr\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<style>body { font-family: Arial, sans-serif; }</style></head><body>"
                + "<h2>Bonjour " + investorName + ",</h2>"
                + "<p>Votre retour sur investissement (ROI) a été traité avec succès.</p>"
                + "<p>Vérifiez votre compte pour voir les détails du virement.</p>"
                + "<p>Si vous ne recevez pas le montant dans les prochains jours, contactez notre service client.</p>"
                + "<br><p>Cordialement,<br>L'équipe Investi</p>"
                + "</body></html>";

        helper.setText(htmlContent, true);
        javaMailSender.send(message);
    }

    // Planification de l'envoi des emails pour chaque retour sur investissement généré

    @Scheduled(cron = "0 0 0 * * ?") // Executes at midnight every day
    public void sendEmails() {
        LocalDate today = LocalDate.now();

        List<Investment> investments = investmentService.findAll();

        for (Investment investment : investments) {
            Date investmentDate = investment.getInvestmentDate(); // This is a java.util.Date

            if (investmentDate != null) {
                // Convert Date to LocalDate
                LocalDate investmentLocalDate = investmentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // Check if 3 months have passed
                if (ChronoUnit.MONTHS.between(investmentLocalDate, today) >= 3) {
                    Investor investor = investment.getInvestor();
                    try {
                        sendHtmlEmail(investor.getEmail(), investor.getFirstName());
                        System.out.println("Email sent to " + investor.getEmail() + " for ROI.");
                    } catch (MessagingException e) {
                        System.err.println("Error sending email to " + investor.getEmail());
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
*/