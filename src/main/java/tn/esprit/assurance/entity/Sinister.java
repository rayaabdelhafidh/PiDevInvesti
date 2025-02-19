package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Sinister {
    public enum SinisterType {
        // Assurance Vie
        DEATH,          // Décès
        DISABILITY,     // Invalidité (permanente ou temporaire)
        CRITICAL_ILLNESS, // Maladie grave (cancer, AVC, etc.)

        // Assurance Crédit
        UNEMPLOYMENT,   // Perte d'emploi
        INSOLVENCY,     // Insolvabilité
        DEFAULT_PAYMENT // Défaut de paiement (échéance non payée)
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("sinister_id")

    private long sinisterId;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    @JsonIgnoreProperties("sinisters")  // Empêche la récursion infinie
    private Contract contract;


    private LocalDate sinisterDate;
    private String description;
    private BigDecimal declaredAmount;
    @Enumerated(EnumType.STRING)
    private SinisterType sinisterType;
    private int severity; // Severity score
    private String dossierStatus; // "En attente", "Accepté", "Refusé"

    @ManyToOne
    @JoinColumn(name = "approvedBy")
    private Insurer approvedBy; // Assureur qui valide le dossier

    @OneToMany(mappedBy = "sinister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents; // Liste des documents envoyés

    @OneToMany(mappedBy = "sinister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compensation> compensations;
    @ManyToOne
    @JoinColumn(name = "provisioning_id")
    private Provisioning provisioning;


}