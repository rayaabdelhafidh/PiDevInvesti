package com.example.investiprojet.services;

import com.itextpdf.io.IOException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.FileSystemResource;
import java.io.File;




@Service
public class EmailService {


    private JavaMailSender mailSender;



    // Injection du JavaMailSender via le constructeur
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithAttachment(String to, String subject, String text, File attachment) throws MessagingException {
        // Créer un message MIME
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Configurer le message
        helper.setFrom("investi.tn@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Ajouter la pièce jointe
        FileSystemResource fileResource = new FileSystemResource(attachment); // Utilisation de File pour l'attachement
        helper.addAttachment("certificat.pdf", fileResource);

        // Envoyer l'email
        mailSender.send(message);
    }

}
