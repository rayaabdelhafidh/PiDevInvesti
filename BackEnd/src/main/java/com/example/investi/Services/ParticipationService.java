package com.example.investi.Services;


import com.example.investi.Entities.Client;
import com.example.investi.Entities.Event;
import com.example.investi.Entities.Participation;
import com.example.investi.Repositories.ClientRepository;
import com.example.investi.Repositories.EventRepository;
import com.example.investi.Repositories.ParticipationRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class ParticipationService {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    // Get all events a client is participating in
    public List<Event> getEventsForClient(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<Participation> participations = participationRepository.findByClient(client);

        return participations.stream()
                .map(Participation::getEvent)
                .collect(Collectors.toList());
    }

    public void generateEventCertificatePdf(Long clientId, Long eventId, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=certificate.pdf");

        // Trouver le client, l'événement et la participation
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        Participation participation = participationRepository.findByClientAndEvent(client, event)
                .orElseThrow(() -> new RuntimeException("Participation non trouvée"));

        // Créer le PdfWriter et le PdfDocument
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
        Document document = new Document(pdfDoc);
        document.setMargins(50, 50, 50, 50);

        // Charger les polices
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Ajouter le logo
        String logoPath = "C:/Users/khali/OneDrive/Bureau/PIDEVINVESTI/images/logo.jpg";
        ImageData logoData = ImageDataFactory.create(logoPath);
        Image logo = new Image(logoData).setWidth(200).setHeight(150).setTextAlignment(TextAlignment.CENTER);
        document.add(logo);

        // Titre du certificat
        document.add(new Paragraph("INVESTI")
                .setFont(fontBold)
                .setFontSize(26)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.BLACK));

        document.add(new Paragraph("Certificat de Participation")
                .setFont(fontBold)
                .setFontSize(22)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.BLUE));

        // Date de génération du certificat
        document.add(new Paragraph("Date : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(fontRegular)
                .setFontSize(12));

        // Espacement
        document.add(new Paragraph("\n"));

        // Texte de l'attestation
        String attestation = String.format("Nous certifions que M./Mme %s %s a pris part activement à l’événement \"%s\", qui s’est tenu du %s au %s, et y a apporté une contribution significative.",
                client.getFirstName(), client.getLastName(), event.getTitle(),
                event.getStartDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                event.getEndDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        document.add(new Paragraph(attestation)
                .setFont(fontRegular)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));

        // Espacement supplémentaire
        document.add(new Paragraph("\n\n"));

        // Ajouter le cachet en bas à droite
        String cachetPath = "C:/Users/khali/OneDrive/Bureau/PIDEVINVESTI/images/cachet.png";
        ImageData cachetData = ImageDataFactory.create(cachetPath);
        Image cachet = new Image(cachetData).setWidth(100).setHeight(100);

        // Table pour le cachet
        Table table = new Table(1);
        table.setWidth(200).setTextAlignment(TextAlignment.RIGHT);
        table.setFixedLayout(); // Utiliser un layout fixe pour mieux contrôler la position

        // Ajouter le cachet en bas à droite
        table.addCell(new Cell().add(cachet).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

        // Ajouter la table avec le cachet au document
        document.add(table);

        // Fermer le document et le writer
        document.close();
        writer.close(); // Assurez-vous de fermer le writer

        // Récupérer l'email du client
        //String clientEmail = client.getEmail();

        /*if (clientEmail != null && !clientEmail.isEmpty()) {
            // Sauvegarder le PDF en fichier temporaire
            File tempFile = new File("certificate.pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                // Sauvegarder le contenu du PDF depuis ByteArrayOutputStream
                byteArrayOutputStream.writeTo(fos);
            }*/
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        // Créer un fichier temporaire à partir du byte array
        File tempFile = File.createTempFile("certificate_", ".pdf");
        Files.write(tempFile.toPath(), pdfBytes);

        String staticEmail = "khadijakhalili779@gmail.com"; // Email statique configuré


            // Envoi de l'email avec le PDF en pièce jointe
        try {
            emailService.sendEmailWithAttachment(staticEmail, "Certificat de participation",
                    "Veuillez trouver ci-joint votre certificat de participation.", tempFile);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        } finally {
            // Supprimer le fichier temporaire après l'envoi de l'email
            tempFile.delete();
        }


        }
    }



