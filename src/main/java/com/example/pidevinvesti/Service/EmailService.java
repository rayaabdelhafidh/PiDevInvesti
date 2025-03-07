package com.example.pidevinvesti.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Remplacer javax.mail par jakarta.mail
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("investi.tn@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    public void sendEmailWithQrCode(String to, String subject, String text, String qrCodeImagePath) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("investi.tn@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        File qrCodeFile = new File(qrCodeImagePath);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        try {
            helper.setFrom("tonemail@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment("QR Code", qrCodeFile);  // Attacher le QR Code à l'email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public class QrCodeGenerator {

        public static String generateQrCodeImage(String data) throws Exception {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < 200; j++) {
                    image.setRGB(i, j, matrix.get(i, j) ? 0x000000 : 0xFFFFFF); // Noir ou Blanc
                }
            }
            File file = new File("qrcode.png");
            ImageIO.write(image, "PNG", file);
            return file.getAbsolutePath();
        }
    }

}

