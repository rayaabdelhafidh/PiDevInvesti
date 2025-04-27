package com.example.investi.Services;

import com.example.investi.Entities.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IDocumentServices {
    Document addDocument(Long sinisterId, MultipartFile file) throws IOException;

    List<Document> getDocumentsBySinister(Long sinisterId);

    void deleteDocument(Long documentId) throws IOException;
}
