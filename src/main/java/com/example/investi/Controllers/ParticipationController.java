package com.example.investi.Controllers;


import com.example.investi.Services.ParticipationService;
import com.itextpdf.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("participation")

public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @GetMapping("/certificat/{clientId}/{eventId}")
    public void generateCertificate(@PathVariable Long clientId, @PathVariable Long eventId, HttpServletResponse response) {
        try {
            participationService.generateEventCertificatePdf(clientId, eventId, response);
        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException("Erreur lors de la génération du certificat PDF", e);
        }
    }

}
