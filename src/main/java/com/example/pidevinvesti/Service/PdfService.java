package com.example.pidevinvesti.Service;


import com.example.pidevinvesti.Entity.Demand;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class PdfService {

    public void generateProfessionalContractPdf(Demand demand) throws IOException {
        String fileName = "Credit de " + demand.getUser().getName() + ".pdf";
        String path = "C:\\wamp64\\www\\pdfs\\" + fileName;  // Ajouter le séparateur de dossier

        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        String logoPath = "C:\\Users\\Lenovo\\Desktop\\LOGO.png";
        Image logo = new Image(ImageDataFactory.create(logoPath));
        logo.setWidth(200);  // Définir la largeur du logo
        logo.setHeight(200);  // Définir la hauteur du logo
        document.add(logo);

        document.add(new Paragraph("Investi")
                .setFont(fontBold)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Contrat de credit")
                .setFont(fontBold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.BLUE));

        document.add(new Paragraph("Date de création: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(fontRegular)
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Parties :")
                .setFont(fontBold)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("Client: " + demand.getUser().getName())
                .setFont(fontRegular)
                .setFontSize(12));
        document.add(new Paragraph("Société: Investi")
                .setFont(fontRegular)
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Détails du Contrat:")
                .setFont(fontBold)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.LEFT));

        Table table = new Table(2);

        Cell headerCell1 = new Cell().add(new Paragraph("ID Credit")).setBold();
        Cell headerCell2 = new Cell().add(new Paragraph(String.valueOf(demand.getDemandId())));

        Cell headerCell3 = new Cell().add(new Paragraph("Montant du prêt ")).setBold();
        Cell headerCell4 = new Cell().add(new Paragraph(String.format("%.2f", demand.getAmount())));

        Cell headerCell5 = new Cell().add(new Paragraph("Durée du prêt")).setBold();
        Cell headerCell6 = new Cell().add(new Paragraph(String.valueOf(demand.getDuration())));

        Cell headerCell7 = new Cell().add(new Paragraph("Date de début du contrat ")).setBold();
        Cell headerCell8 = new Cell().add(new Paragraph(demand.getDemandDate().toString()));

        table.addCell(headerCell1);
        table.addCell(headerCell2);
        table.addCell(headerCell3);
        table.addCell(headerCell4);
        table.addCell(headerCell5);
        table.addCell(headerCell6);
        table.addCell(headerCell7);
        table.addCell(headerCell8);

        table.setWidth(500);
        table.getCell(0, 0).setWidth(100);
        table.getCell(1, 0).setWidth(200);

        document.add(table);

        document.add(new Paragraph("Conditions Générales :")
                .setFont(fontBold)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.LEFT));
        document.add(new Paragraph("1. Le présent contrat prend effet à compter de la date de signature par les deux parties.")
                .setFont(fontRegular)
                .setFontSize(12));
        document.add(new Paragraph("2. Le remboursement du prêt s'effectue par mensualités constantes sur la durée totale du crédit.")
                .setFont(fontRegular)
                .setFontSize(12));
        document.add(new Paragraph("3. Tout retard de paiement supérieur à 15 jours entraîne des pénalités fixées à 2% du montant de la mensualité due.\n")
                .setFont(fontRegular)
                .setFontSize(12));
        document.add(new Paragraph("4. En cas de non-paiement prolongé, la société Investi se réserve le droit d'engager des procédures de recouvrement conformément à la législation en vigueur.\n")
                .setFont(fontRegular)
                .setFontSize(12));

        String signaturePath = "C:\\Users\\Lenovo\\Desktop\\signature.png";
        Image signature = new Image(ImageDataFactory.create(signaturePath));
        signature.setWidth(200);
        signature.setHeight(100);

        Table signatureTable = new Table(new float[]{1, 2}); // Deux colonnes
        signatureTable.setWidth(UnitValue.createPercentValue(100)); // Largeur à 100%

        Cell textCell = new Cell().add(new Paragraph("Signature électronique :").setFont(fontBold).setFontSize(12));
        textCell.setBorder(Border.NO_BORDER);
        Cell imageCell = new Cell().add(signature.setAutoScale(true));
        imageCell.setBorder(Border.NO_BORDER);
        signatureTable.addCell(textCell);
        signatureTable.addCell(imageCell);
        document.add(signatureTable);

        document.close();
    }

}
