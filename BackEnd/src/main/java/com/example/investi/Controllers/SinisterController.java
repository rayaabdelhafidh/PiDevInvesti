package com.example.investi.Controllers;

import com.example.investi.Entities.Document;
import com.example.investi.Entities.Sinister;
import com.example.investi.Services.FraudDetectionService;
import com.example.investi.Services.SinisterServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sinisters")
@RequiredArgsConstructor
public class SinisterController {
@Autowired
    private SinisterServices sinisterService;
// dyzz slib lpuh wfsp
    @Autowired
    private FraudDetectionService fraudDetectionService;

    // Ajouter un sinistre
    @PostMapping("/add")
    public Sinister addSinister(@RequestBody Sinister sinister,@RequestParam(required = false) Long ContractId) {
        return sinisterService.addSinister(sinister,ContractId);
    }


    // Récupérer tous les sinistres
    @GetMapping
    public List<Sinister> getAllSinisters() {
     return sinisterService.getAllSinisters();
    }

    // Récupérer un sinistre par ID
    @GetMapping("/{id}")
    public ResponseEntity<Sinister> getSinisterById(@PathVariable Long id) {
        Optional<Sinister> sinister = sinisterService.getSinisterById(id);
        return sinister.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modifier un sinistre
   /* @PutMapping("/update")

    public ResponseEntity<Sinister> updateSinister(@RequestBody Sinister sinister) {
        Sinister updatedSinister = sinisterService.updateSinister(sinister);
        return ResponseEntity.ok(updatedSinister);
    }*/

    // Supprimer un sinistre
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSinister(@PathVariable Long id) {
        sinisterService.deleteSinister(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{sinisterId}/documents")
    public ResponseEntity<List<Document>> getDocumentsBySinister(@PathVariable Long sinisterId) {
        List<Document> documents = sinisterService.getDocumentsBySinister(sinisterId);
        return ResponseEntity.ok(documents);
    }
    @PostMapping(value = "/declare", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Sinister> declareSinister(
            @RequestPart("sinister") @ModelAttribute Sinister sinister,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        Sinister savedSinister = sinisterService.addSinisterWithDocuments(sinister, files);
        return ResponseEntity.ok(savedSinister);
    }

    @PutMapping("/{sinisterId}")
    public ResponseEntity<Sinister> updateSinisterByClient(
            @PathVariable Long sinisterId, @RequestBody Sinister sinisterDetails) {
        Sinister updatedSinister = sinisterService.updateSinisterByClient(sinisterId, sinisterDetails);
        return ResponseEntity.ok(updatedSinister);
    }

    @PutMapping("/{sinisterId}/status")
    public ResponseEntity<Sinister> updateSinisterStatus(
            @PathVariable Long sinisterId, @RequestParam("status") Sinister.DossierStatus status) {
        Sinister updatedSinister = sinisterService.updateSinisterStatus(sinisterId, status);
        return ResponseEntity.ok(updatedSinister);
    }
    /*@PostMapping("/checkFraud")
    public ResponseEntity<String> detectFraud(@RequestBody Sinister sinistre) {
        // Extraire les informations nécessaires du sinistre
        String description = sinistre.getDescription();
        double montantDeclare = sinistre.getDeclaredAmount();
        int gravite = sinistre.getSeverity();

        // Appel au service de détection de fraude
        boolean isFraudulent = fraudDetectionService.detectFraud(description, montantDeclare, gravite);

        // Retourner le résultat de la détection
        return ResponseEntity.ok(isFraudulent ? "Fraudulent" : "Non Fraudulent");
    }*/

}
