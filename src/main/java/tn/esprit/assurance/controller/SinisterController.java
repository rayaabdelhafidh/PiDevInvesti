package tn.esprit.assurance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.assurance.entity.Sinister;
import tn.esprit.assurance.services.SinisterServices;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sinisters")
@RequiredArgsConstructor
public class SinisterController {
@Autowired
    private SinisterServices sinisterService;

    // Ajouter un sinistre
    @PostMapping
    public ResponseEntity<Sinister> addSinister(@RequestBody Sinister sinister) {
        Sinister savedSinister = sinisterService.addSinister(sinister);
        return ResponseEntity.ok(savedSinister);
    }

    // Récupérer tous les sinistres
    @GetMapping
    public ResponseEntity<List<Sinister>> getAllSinisters() {
        return ResponseEntity.ok(sinisterService.getAllSinisters());
    }

    // Récupérer un sinistre par ID
    @GetMapping("/{id}")
    public ResponseEntity<Sinister> getSinisterById(@PathVariable Long id) {
        Optional<Sinister> sinister = sinisterService.getSinisterById(id);
        return sinister.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modifier un sinistre
    @PutMapping("/{id}")
    public ResponseEntity<Sinister> updateSinister(@PathVariable Long id, @RequestBody Sinister sinister) {
        Sinister updatedSinister = sinisterService.updateSinister(id, sinister);
        return ResponseEntity.ok(updatedSinister);
    }

    // Supprimer un sinistre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSinister(@PathVariable Long id) {
        sinisterService.deleteSinister(id);
        return ResponseEntity.noContent().build();
    }
}
