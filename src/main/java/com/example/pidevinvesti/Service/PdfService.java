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
        // Nom du fichier PDF
        String fileName = "Credit de " + demand.getUser().getName() + ".pdf";
        String path = "C:\\Users\\LENOVO\\Desktop\\" + fileName;

        // Création du document PDF
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Charger la police Helvetica
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Ajouter un logo au début du document
        String logoPath = "C:\\Users\\Lenovo\\Desktop\\LOGO.png";  // Si le logo est dans le même répertoire que le fichier .java

        Image logo = new Image(ImageDataFactory.create(logoPath));
        logo.setWidth(200); // Définir la largeur du logo
        logo.setHeight(200); // Définir la hauteur du logo

// Ajouter le logo au document
        document.add(logo);


        // En-tête : Nom de l'entreprise et le titre du contrat
        document.add(new Paragraph("Investi")
                .setFont(fontBold)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Contrat de credit")
                .setFont(fontBold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.BLUE));

        // Date de création
        document.add(new Paragraph("Date de création: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFont(fontRegular)
                .setFontSize(12));

        // Espacement
        document.add(new Paragraph("\n"));

        // Informations sur les parties
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

        // Espacement
        document.add(new Paragraph("\n"));

        // Détails du contrat
        document.add(new Paragraph("Détails du Contrat:")
                .setFont(fontBold)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.LEFT));

        // Créer un tableau avec une mise en page professionnelle
        Table table = new Table(2); // Crée un tableau avec 2 colonnes

        // Ajouter des cellules d'en-tête en gras (sans couleur)
          Cell headerCell1 = new Cell().add(new Paragraph("ID Credit")).setBold();
          Cell headerCell2 = new Cell().add(new Paragraph(String.valueOf(demand.getDemandId())));

        Cell headerCell3 = new Cell().add(new Paragraph("Montant du prêt ")).setBold();
        Cell headerCell4 = new Cell().add(new Paragraph(String.format("%.2f", demand.getAmount())));

        Cell headerCell5 = new Cell().add(new Paragraph("Durée du prêt")).setBold();
        Cell headerCell6 = new Cell().add(new Paragraph(String.valueOf(demand.getDuration())));

        Cell headerCell7 = new Cell().add(new Paragraph("Date de début du contrat ")).setBold();
        Cell headerCell8 = new Cell().add(new Paragraph(demand.getDemandDate().toString()));

        //  Cell headerCell9 = new Cell().add(new Paragraph("Date de Fin")).setBold();
        //  Cell headerCell10 = new Cell().add(new Paragraph(contract.getEndDate().toString()));

        // Ajout des cellules au tableau
        table.addCell(headerCell1);
        table.addCell(headerCell2);
        table.addCell(headerCell3);
        table.addCell(headerCell4);
        table.addCell(headerCell5);
        table.addCell(headerCell6);
        table.addCell(headerCell7);
        table.addCell(headerCell8);
        // table.addCell(headerCell9);
        // table.addCell(headerCell10);

        // Ajuster les largeurs des colonnes manuellement
        table.setWidth(500);
        table.getCell(0, 0).setWidth(100); // Largeur spécifique pour chaque cellule
        table.getCell(1, 0).setWidth(200); // Largeur spécifique pour chaque cellule

        // Ajout du tableau au document
         document.add(table);

        // Conditions générales
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
        document.add(new Paragraph("5. Le client autorise Investi à prélever automatiquement les mensualités sur le compte associé mentionné lors de la demande\n")
                .setFont(fontRegular)
                .setFontSize(12));
        document.add(new Paragraph("6. Le client s'engage à informer Investi de toute modification concernant ses coordonnées bancaires ou personnelles pendant toute la durée du contrat.\n")
                .setFont(fontRegular)
                .setFontSize(12));

        // Chemin de l'image de la signature (modifie avec le bon chemin)
        String signaturePath = "C:\\Users\\Lenovo\\Desktop\\signature.png";

// Charger l'image de la signature
        Image signature = new Image(ImageDataFactory.create(signaturePath));

// Ajuster la taille de la signature
        signature.setWidth(200);
        signature.setHeight(100);

// Créer une table pour aligner "Signature électronique :" à gauche et l'image à droite
        Table signatureTable = new Table(new float[]{1, 2}); // Deux colonnes : texte (petit) et image (plus grande)
        signatureTable.setWidth(UnitValue.createPercentValue(100)); // Largeur à 100%



        // Ajouter la signature électronique
        Cell textCell = new Cell().add(new Paragraph("Signature électronique :").setFont(fontBold).setFontSize(12));
        textCell.setBorder(Border.NO_BORDER); // Supprimer la bordure
        // Ajouter l'image dans la deuxième colonne
        Cell imageCell = new Cell().add(signature.setAutoScale(true));
        imageCell.setBorder(Border.NO_BORDER); // Supprimer la bordure

// Ajouter les cellules au tableau
        signatureTable.addCell(textCell);
        signatureTable.addCell(imageCell);

// Ajouter le tableau au document
        document.add(signatureTable);

        // Fermer le document
        document.close();
    }
}
