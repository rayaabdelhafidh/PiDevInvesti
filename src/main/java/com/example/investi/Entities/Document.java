package com.example.investi.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne
    @JoinColumn(name = "sinisterId", nullable = false)
    private Sinister sinister;

   // private String documentType; // "Facture", "Ordonnance", "Compte-rendu médical"
    private String documentPath; // Chemin du fichier stocké
    private LocalDate uploadDate;
}
