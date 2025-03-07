package com.example.investi.Entities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("investi.tn@gmail.com");
        mailSender.setPassword("dyzzsliblpuhwfsp");  // Assurez-vous que le mot de passe est correct
        mailSender.getJavaMailProperties().put("mail.smtp.auth", "true");
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
        mailSender.getJavaMailProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return mailSender;
    }
}

