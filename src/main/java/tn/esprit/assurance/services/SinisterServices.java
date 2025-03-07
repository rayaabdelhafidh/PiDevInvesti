package tn.esprit.assurance.services;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.assurance.entity.Compensation;
import tn.esprit.assurance.entity.Document;
import tn.esprit.assurance.entity.Sinister;
import tn.esprit.assurance.repositories.CompensationRepository;
import tn.esprit.assurance.repositories.DocumentRepository;
import tn.esprit.assurance.repositories.SinisterRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class SinisterServices implements ISinisterServices {

    @Autowired
    private SinisterRepository sinisterRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private CompensationRepository compensationRepository;  // Injection du repository

    // ✅ Ajouter un sinistre seul
    public Sinister addSinister(Sinister sinister) {
        sinister.setDossierStatus(Sinister.DossierStatus.PENDING);
        sinister.setSeverity(setSeverityBasedOnSinisterType(sinister));
        return sinisterRepository.save(sinister);
    }

    // ✅ Ajouter un sinistre avec des documents
    @Transactional
    public Sinister addSinisterWithDocuments(Sinister sinister, List<MultipartFile> files) throws IOException {
        // 📌 Étape 1 : Enregistrer le sinistre
        Sinister savedSinister = sinisterRepository.save(sinister);

        // 📌 Étape 2 : Sauvegarde des documents si présents
        if (files != null && !files.isEmpty()) {
            List<Document> documents = new ArrayList<>();
            for (MultipartFile file : files) {
                Document document = saveDocument(file, savedSinister);
                documents.add(document);
            }
            documentRepository.saveAll(documents);
        }

        return savedSinister;
    }

    // ✅ Méthode privée pour sauvegarder un document
    private Document saveDocument(MultipartFile file, Sinister sinister) throws IOException {
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        String storagePath = "uploads/sinisters/" + uniqueFileName;

        // 📌 Créer le dossier s'il n'existe pas
        Files.createDirectories(Path.of("uploads/sinisters/"));

        // 📌 Copier le fichier vers l'emplacement final
        Path filePath = Path.of(storagePath);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 📌 Créer et retourner l'objet `Document`
        Document document = new Document();
        document.setSinister(sinister);
        document.setOriginalFilename(file.getOriginalFilename());
        document.setDocumentPath(storagePath);
        document.setFileType(file.getContentType());

        return document;
    }

    // ✅ Récupérer tous les sinistres
    public List<Sinister> getAllSinisters() {
        return sinisterRepository.findAll();
    }

    // ✅ Récupérer un sinistre par ID
    public Optional<Sinister> getSinisterById(Long id) {
        return sinisterRepository.findById(id);
    }

    // ✅ Modifier un sinistre
    @Override
    @Transactional
    public Sinister updateSinister(Sinister sinisterDetails) {
        return sinisterRepository.save(sinisterDetails);
    }
    @Transactional
    public Sinister updateSinisterByClient(Long sinisterId, Sinister sinisterDetails) {
        Sinister sinister = sinisterRepository.findById(sinisterId)
                .orElseThrow(() -> new IllegalArgumentException("Sinistre introuvable !"));

        if (sinister.getDossierStatus() != Sinister.DossierStatus.PENDING) {
            throw new IllegalStateException("Vous ne pouvez plus modifier ce sinistre.");
        }



        return sinisterRepository.save(sinister);
    }


    @Transactional
    public Sinister updateSinisterStatus(Long sinisterId, Sinister.DossierStatus status) {
        Sinister sinister = sinisterRepository.findById(sinisterId)
                .orElseThrow(() -> new IllegalArgumentException("Sinistre introuvable !"));

        if (sinister.getDossierStatus() != Sinister.DossierStatus.PENDING) {
            throw new IllegalStateException("Ce sinistre a déjà été traité.");
        }

        // 📌 Mettre à jour le statut du sinistre
        sinister.setDossierStatus(status);
        sinisterRepository.save(sinister);

        // 📌 Si le sinistre est accepté, on crée une ou plusieurs compensations
        if (status == Sinister.DossierStatus.ACCEPTED) {
            double totalCompensation = getEstimatedAmount(sinister); // Montant estimé du remboursement
            int severity = sinister.getSeverity(); // Récupérer la sévérité du sinistre
            List<Compensation> compensations = new ArrayList<>();

            // 📌 Diviser la compensation selon la sévérité
            switch (severity) {
                case 1:
                    // Compensation complète en une seule tranche
                    compensations.add(createCompensation(sinister, totalCompensation, 1));
                    break;
                case 2:
                    // Compensation en deux tranches égales
                    compensations.add(createCompensation(sinister, totalCompensation / 2, 1));
                    compensations.add(createCompensation(sinister, totalCompensation / 2, 2));
                    break;
                case 3:
                    // Compensation en trois tranches espacées de 3 mois
                    compensations.add(createCompensation(sinister, totalCompensation * 0.4, 1));
                    compensations.add(createCompensation(sinister, totalCompensation * 0.3, 2));
                    compensations.add(createCompensation(sinister, totalCompensation * 0.3, 3));
                    break;
                default:
                    throw new IllegalArgumentException("Sévérité inconnue !");
            }

            // 📌 Enregistrer toutes les compensations
            compensationRepository.saveAll(compensations);
        }

        // 📌 Envoyer un email de notification au client
        sendNotificationEmail(sinister);

        return sinister;
    }

    // Méthode pour créer une compensation
    //@Scheduled(cron = "0 0 0 * * ?")
    private Compensation createCompensation(Sinister sinister, double amount, int tranche) {
        Compensation compensation = new Compensation();
        compensation.setSinister(sinister);
        compensation.setCompensatedAmount(amount);

        // 📅 Calculer la date de compensation espacée de 3 mois pour chaque tranche
        compensation.setCompensationDate(LocalDate.now().plusMonths(3 * (tranche - 1))); // Décaler de 3 mois pour chaque tranche
        compensation.setSettlementDelay((int) ChronoUnit.DAYS.between(LocalDate.now(), compensation.getCompensationDate())/3);

        // Ajouter un statut "En attente" par défaut
        compensation.setDefaultStatus();

        return compensation;
    }
    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "0 0 0 * * ?")  // Exécution quotidienne à minuit
    public void updateSettlementDelays() {
        List<Compensation> compensations = compensationRepository.findAll();

        for (Compensation compensation : compensations) {
            if (compensation.getSettlementDelay() > 0) {
                compensation.setSettlementDelay(compensation.getSettlementDelay() - 1); // Décrémenter de 1
            }
        }

        compensationRepository.saveAll(compensations);
        System.out.println("✅ Settlement delays updated at midnight.");
    }

    // Méthode pour calculer le délai de remboursement


    public double getEstimatedAmount(Sinister sinister) {
        double declaredAmount = sinister.getDeclaredAmount();
        int severity = sinister.getSeverity();  // Sévérité du sinistre (1, 2 ou 3)

        switch (severity) {
            case 1:
                return declaredAmount * 0.5;  // 50% pour la sévérité 1
            case 2:
                return declaredAmount * 0.75; // 75% pour la sévérité 2
            case 3:
                return declaredAmount;        // 100% pour la sévérité 3
            default:
                return 0.0; // Sécurité si la sévérité est invalide
        }
    }


    // Simulation de l'envoi d'un email
    private void sendNotificationEmail(Sinister sinister) {
        if (sinister.getContract().getClient() == null || sinister.getContract().getClient().getEmail() == null) {
            System.out.println("❌ Impossible d'envoyer l'email : Client ou email introuvable.");
            return;
        }

        String message;
        String subject = "Mise à jour de votre sinistre";

        if (sinister.getDossierStatus() == Sinister.DossierStatus.ACCEPTED) {
            message = "✅ Votre sinistre a été accepté.";
        } else if (sinister.getDossierStatus() == Sinister.DossierStatus.REJECTED) {
            message = "❌ Votre sinistre a été refusé.";
        } else {
            message = "ℹ️ Votre sinistre est en cours de traitement.";
        }

        try {
            emailService.sendEmail(sinister.getContract().getClient().getEmail(), subject, message);
            System.out.println("📧 Email envoyé à " + sinister.getContract().getClient().getEmail());
        } catch (MessagingException e) {
            System.out.println("❌ Erreur d'envoi de l'email : " + e.getMessage());
        }
    }
    private int setSeverityBasedOnSinisterType(Sinister sinister) {
        switch (sinister.getSinisterType()) {
            case DEATH:
            case CRITICAL_ILLNESS:
                return 3; // Plus grave
            case DISABILITY:
            case INSOLVENCY:
                return 2; // Modéré
            case UNEMPLOYMENT:
            case DEFAULT_PAYMENT:
                return 1; // Moins grave
            default:
                return 1; // Par défaut
        }
    }


    // ✅ Supprimer un sinistre
    @Override
    @Transactional
    public void deleteSinister(Long id) {
        documentRepository.deleteBySinisterSinisterId(id);  // Supprime les documents liés

        sinisterRepository.deleteById(id);
    }

    // ✅ Récupérer les documents d'un sinistre donné
    public List<Document> getDocumentsBySinister(Long sinisterId) {
        Optional<Sinister> sinisterOpt = sinisterRepository.findById(sinisterId);
        if (sinisterOpt.isEmpty()) {
            throw new IllegalArgumentException("Sinistre introuvable !");
        }
        return documentRepository.findBySinisterSinisterId(sinisterId);
    }
}
