package tn.esprit.assurance.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.assurance.entity.Document;
import tn.esprit.assurance.services.DocumentServices;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@AllArgsConstructor
public class DocumentController {


@Autowired
        private DocumentServices documentServices;

    @ApiOperation(value = "Upload un document lié à un sinistre")
    @PostMapping("/{sinisterId}")
    public ResponseEntity<Document> uploadDocument(
            @ApiParam(value = "L'ID du sinistre auquel le document est associé", required = true)
            @PathVariable Long sinisterId,

            @ApiParam(value = "Le fichier à télécharger", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            // Passer uniquement le sinisterId et le fichier au service
            Document savedDocument = documentServices.addDocument(sinisterId, file);
            return ResponseEntity.ok(savedDocument);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}


