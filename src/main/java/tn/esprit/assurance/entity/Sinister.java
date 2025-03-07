package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.*;
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

    public enum DossierStatus {
        PENDING,    // "En attente"
        ACCEPTED,   // "Accepté"
        REJECTED    // "Refusé"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("sinister_id")
    private long sinisterId;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    @JsonIgnoreProperties("sinisters")  // Empêche la récursion infinie
    @JsonIgnore
    @JsonProperty("contract")

    private Contract contract;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("sinister_date")
    private LocalDate sinisterDate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("declared_amount")
    private double declaredAmount;

    @Enumerated(EnumType.STRING)
    @JsonProperty("sinister_type")
    private SinisterType sinisterType;

    @JsonProperty("severity")
    private int severity; // Severity score

    @Enumerated(EnumType.STRING)
    @JsonProperty("dossier_status")
    private DossierStatus dossierStatus;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    @JsonProperty("approved_by")
    @JsonIgnore

    private Insurer approvedBy; // Assureur qui valide le dossier

    @OneToMany(mappedBy = "sinister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("sinister")
    @JsonProperty("documents")
    @JsonIgnore

    private List<Document> documents; // Liste des documents envoyés

    @OneToMany(mappedBy = "sinister", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("sinister")
    @JsonProperty("compensations")
    @JsonIgnore

    private List<Compensation> compensations;

    /*
    @ManyToOne
    @JoinColumn(name = "provisioning_id")
    @JsonProperty("provisioning")
    private Provisioning provisioning;
    */

    @PrePersist
    protected void onCreate() {
        if (this.dossierStatus == null) {
            this.dossierStatus = DossierStatus.PENDING; // Défaut = "En attente"
        }
    }
}
