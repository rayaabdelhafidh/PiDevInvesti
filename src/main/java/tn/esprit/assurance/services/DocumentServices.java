package tn.esprit.assurance.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.assurance.entity.Document;
import tn.esprit.assurance.entity.Sinister;
import tn.esprit.assurance.repositories.DocumentRepository;
import tn.esprit.assurance.repositories.SinisterRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class DocumentServices implements IDocumentServices {
@Autowired
    private  DocumentRepository documentRepository;
@Autowired
    private  SinisterRepository sinisterRepository;

    private   String STORAGE_DIRECTORY = "uploads/sinisters"; // 📂 Dossier où stocker les fichiers

    @Override
    @Transactional
    public Document addDocument(Long sinisterId, MultipartFile file) throws IOException {
        // Vérifier si le sinistre existe
        Sinister sinister = sinisterRepository.findById(sinisterId)
                .orElseThrow(() -> new IllegalArgumentException("Sinistre introuvable !"));

        // Obtenir le nom original du fichier et son extension
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // Déterminer le chemin du fichier avec son nom original
        Path filePath = Path.of(STORAGE_DIRECTORY, originalFileName);

        // Créer les répertoires parents si nécessaire
        Files.createDirectories(filePath.getParent());

        // Sauvegarder le fichier sur le disque
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Créer un objet Document avec les informations nécessaires
        Document document = new Document();
        document.setSinister(sinister); // Lier le sinistre
        document.setDocumentPath(filePath.toString()); // Le chemin du fichier
        document.setOriginalFilename(originalFileName); // Nom original
        document.setFileType(file.getContentType()); // Type de fichier
        document.setUploadDate(java.time.LocalDate.now()); // Date d'upload (aujourd'hui)

        // Enregistrer le document dans la base de données
        return documentRepository.save(document);
    }


    @Override
    public List<Document> getDocumentsBySinister(Long sinisterId) {
        return documentRepository.findBySinisterSinisterId(sinisterId);
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId) throws IOException {
        Optional<Document> documentOpt = documentRepository.findById(documentId);
        if (documentOpt.isEmpty()) {
            throw new IllegalArgumentException("Document introuvable !");
        }

        Document document = documentOpt.get();
        Path filePath = Path.of(document.getDocumentPath());

        // 🔹 Supprimer le fichier du disque
        Files.deleteIfExists(filePath);

        // 🔹 Supprimer le document de la base
        documentRepository.delete(document);
    }
}
