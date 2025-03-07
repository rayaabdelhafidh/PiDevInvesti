package tn.esprit.assurance.services;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.UnitValue;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.assurance.entity.Client;
import tn.esprit.assurance.entity.Contract;
import tn.esprit.assurance.repositories.ClientRepository;
import tn.esprit.assurance.repositories.ContractRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;

import java.time.LocalDate;



import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;


import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ContractServices implements IContractServices {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Contract addContract(Long clientId, Contract contract) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Calculer la prime annuelle avant application des remises
        double annualPremium = calculateAnnualPremium(client, contract.getContractType());

        // Appliquer les remises dynamiques après le calcul de la prime
        annualPremium = applyDiscounts(client, annualPremium);

        // Affecter la prime calculée et autres paramètres du contrat
        contract.setAnnualPremium(annualPremium);

        // Définir les dates de début et de fin
        if (contract.getStatus() == Contract.ContractStatus.ACTIVE) {
            contract.setStartDate(LocalDate.now());
            contract.setEndDate(LocalDate.now().plusYears(1));
        }

        contract.setClient(client);
        contract.setInsuredAmount(calculateInsuredAmount(contract));  // Associer le montant assuré
        return contractRepository.save(contract);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    @Override
    @Transactional
    public Contract updateContract(Long id, Contract updatedContract) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        if (updatedContract.getAnnualPremium()!=0.0) {
            existingContract.setAnnualPremium(updatedContract.getAnnualPremium());
        }
        if (updatedContract.getInsuredAmount()!=0.0) {
            existingContract.setInsuredAmount(updatedContract.getInsuredAmount());
        }

        // Mise à jour uniquement des champs non nuls
        if (updatedContract.getContractType() != null) {
            existingContract.setContractType(updatedContract.getContractType());
        }

        if (updatedContract.getClient() != null) {
            existingContract.setClient(updatedContract.getClient());
        }

        if (updatedContract.getStatus() != null) {
            existingContract.setStatus(updatedContract.getStatus());
            if (existingContract.getStatus() == Contract.ContractStatus.ACTIVE) {
                existingContract.setStartDate(LocalDate.now());
                existingContract.setEndDate(LocalDate.now().plusYears(1));

                // Génération du PDF quand le contrat devient ACTIF
               try {
                   generateProfessionalContractPdf(existingContract);  // Appel à la méthode de génération de PDF
                } catch (IOException e) {
                    throw new RuntimeException("Erreur lors de la génération du PDF du contrat.", e);
                }
            }
        }

        return contractRepository.save(existingContract);
    }
    public void generateProfessionalContractPdf(Contract contract) throws IOException {
        // Nom du fichier PDF
        String fileName = "Contract_" + contract.getContractId() + ".pdf";
        String path = "C:\\Users\\MSI\\Desktop\\Contrats\\" + fileName;

        // Création du document PDF
        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Charger la police Helvetica
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Ajouter un logo au début du document
        String logoPath = "C:\\Users\\MSI\\Desktop\\LOGO.png";  // Si le logo est dans le même répertoire que le fichier .java

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

        document.add(new Paragraph("Contrat d'Assurance")
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

        document.add(new Paragraph("Client: " + contract.getClient().getJoinDate())
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
        Cell headerCell1 = new Cell().add(new Paragraph("ID Contrat")).setBold();
        Cell headerCell2 = new Cell().add(new Paragraph(String.valueOf(contract.getContractId())));

        Cell headerCell3 = new Cell().add(new Paragraph("Prime Annuelle")).setBold();
        Cell headerCell4 = new Cell().add(new Paragraph(String.format("%.2f", contract.getAnnualPremium())));

        Cell headerCell5 = new Cell().add(new Paragraph("Type de Contrat")).setBold();
        Cell headerCell6 = new Cell().add(new Paragraph(contract.getContractType().name()));

        Cell headerCell7 = new Cell().add(new Paragraph("Date de Début")).setBold();
        Cell headerCell8 = new Cell().add(new Paragraph(contract.getStartDate().toString()));

        Cell headerCell9 = new Cell().add(new Paragraph("Date de Fin")).setBold();
        Cell headerCell10 = new Cell().add(new Paragraph(contract.getEndDate().toString()));

        // Ajout des cellules au tableau
        table.addCell(headerCell1);
        table.addCell(headerCell2);
        table.addCell(headerCell3);
        table.addCell(headerCell4);
        table.addCell(headerCell5);
        table.addCell(headerCell6);
        table.addCell(headerCell7);
        table.addCell(headerCell8);
        table.addCell(headerCell9);
        table.addCell(headerCell10);

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

        document.add(new Paragraph("1. Le contrat prend effet à compter de la date de signature.")
                .setFont(fontRegular)
                .setFontSize(12));
        document.add(new Paragraph("2. La prime annuelle est due au moment de l'activation du contrat.")
                .setFont(fontRegular)
                .setFontSize(12));

        // Chemin de l'image de la signature (modifie avec le bon chemin)
        String signaturePath = "C:\\Users\\MSI\\Desktop\\signature.png";

// Charger l'image de la signature
        Image signature = new Image(ImageDataFactory.create(signaturePath));

// Ajuster la taille de la signature
        signature.setWidth(200);
        signature.setHeight(100);

// Créer une table pour aligner "Signature électronique :" à gauche et l'image à droite
        Table signatureTable = new Table(new float[]{1, 2}); // Deux colonnes : texte (petit) et image (plus grande)
        signatureTable.setWidth(UnitValue.createPercentValue(100)); // Largeur à 100%

// Ajouter le texte "Signature électronique :"
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

    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    @Override
    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    // Méthode pour calculer la prime annuelle de base selon l'âge et le type d'assurance
    public double calculateAnnualPremium(Client client, Contract.InsuranceType insuranceType) {
        int age = LocalDate.now().getYear() - client.getDateOfBirth().getYear();
        double basePremium = 0.0;

        Set<String> highRiskProfessions = Set.of("chauffeur", "pilote", "pompier", "policier", "travailleur en hauteur");

        if (insuranceType == Contract.InsuranceType.HEALTH) {
            basePremium = 400.0;

            if (age < 25) {
                basePremium *= 1.3; // Augmentation de 30% pour les jeunes
            } else if (age > 50) {
                basePremium *= 1.5; // Augmentation de 50% pour les seniors
            }

            if (client.getGender() == Client.Sexe.Feminin) {
                basePremium *= 0.95; // Réduction de 5% pour les femmes
            }

        } else if (insuranceType == Contract.InsuranceType.CREDIT) {
            basePremium = 800.0;

            if (age < 30) {
                basePremium *= 1.5; // Augmentation de 50% pour les jeunes
            } else if (age > 60) {
                basePremium *= 1.8; // Augmentation de 80% pour les seniors
            }
        }

        // Ajustement selon la profession
        String profession = client.getProfession().trim().toLowerCase();
        if (highRiskProfessions.contains(profession)) {
            basePremium *= 1.4; // Augmentation de 40% pour les métiers à risque
        }

        return Math.round(basePremium * 100.0) / 100.0; // Arrondi à 2 décimales
    }

    // Méthode pour appliquer les remises dynamiques
    private double applyDiscounts(Client client, double annualPremium) {
        // Appliquer la remise fidélité
        annualPremium = applyLoyaltyDiscount(client, annualPremium);

        // Appliquer la remise basée sur le nombre de contrats
        annualPremium = applyMultipleContractsDiscount(client, annualPremium);

        // Appliquer les remises saisonnières (par exemple, Noël)
        annualPremium = applySeasonalDiscount(annualPremium);

        return annualPremium;
    }

    // Remise de fidélité (ancienneté)
    private double applyLoyaltyDiscount(Client client, double annualPremium) {
        int yearsWithCompany = LocalDate.now().getYear() - client.getJoinDate().getYear();

        if (yearsWithCompany >= 5 && yearsWithCompany < 10) {
            annualPremium *= 0.95; // Remise de 5% pour 5-10 ans
        } else if (yearsWithCompany >= 10) {
            annualPremium *= 0.90; // Remise de 10% pour 10 ans et plus
        }

        return annualPremium;
    }

    // Remise basée sur le nombre de contrats du client
    private double applyMultipleContractsDiscount(Client client, double annualPremium) {
        long contractCount = contractRepository.countByClient(client);

        if (contractCount == 2) {
            annualPremium *= 0.95; // Remise de 5% pour 2 contrats
        } else if (contractCount >= 3) {
            annualPremium *= 0.90; // Remise de 10% pour 3 contrats ou plus
        }

        return annualPremium;
    }

    // Remise saisonnière (par exemple, Noël)
    private double applySeasonalDiscount(double annualPremium) {
        LocalDate now = LocalDate.now();
        LocalDate startPromo = LocalDate.of(now.getYear(), 12, 1); // Début de la promotion en décembre
        LocalDate endPromo = LocalDate.of(now.getYear(), 12, 31); // Fin de la promotion

        if ((now.isEqual(startPromo) || now.isAfter(startPromo)) && now.isBefore(endPromo)) {
            annualPremium *= 0.85; // Remise de 15% pendant la promotion de Noël
        }

        return annualPremium;
    }

    // Méthode pour calculer le montant assuré
    public double calculateInsuredAmount(Contract contract) {
        int age = LocalDate.now().getYear() - contract.getClient().getDateOfBirth().getYear();
        double insuredAmount = 5000;

        if (contract.getContractType() == Contract.InsuranceType.HEALTH) {
            insuredAmount += 10000;
            if (age < 25) {
                insuredAmount += 2000;
            } else if (age > 50) {
                insuredAmount += 3000;
            }
        } else if (contract.getContractType() == Contract.InsuranceType.CREDIT) {
            insuredAmount += 50000;
            if (contract.getAnnualPremium() > 10000) {
                insuredAmount += 10000;
            }
        }

        String profession = contract.getClient().getProfession().trim().toLowerCase();
        Set<String> highRiskProfessions = Set.of("chauffeur", "pilote", "pompier", "policier", "travailleur en hauteur");
        if (highRiskProfessions.contains(profession)) {
            insuredAmount += 5000;
        }

        insuredAmount += contract.getAnnualPremium() * 2;
        return Math.min(insuredAmount, 100000);
    }
}
