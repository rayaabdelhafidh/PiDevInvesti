package tn.esprit.assurance.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.assurance.entity.Document;

import java.io.IOException;
import java.util.List;

public interface IDocumentServices {
    Document addDocument(Long sinisterId, MultipartFile file) throws IOException;

    List<Document> getDocumentsBySinister(Long sinisterId);

    void deleteDocument(Long documentId) throws IOException;
}
