package com.example.investi.Entities;

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

public class Contract {
    public enum InsuranceType {
        HEALTH,  // Assurance Santé
        CREDIT   // Assurance Crédit
    }
    public enum ContractStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        CANCELLED
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonProperty("contract_id") // Convertit contractId en "contract_id" dans JSON
    private long contractId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;


    @Enumerated(EnumType.STRING)
    @JsonProperty("contract_type")
    private InsuranceType contractType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("insured_amount")
    private double insuredAmount;

    @JsonProperty("annual_premium")
    private double annualPremium;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private ContractStatus status;


    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("contract")  // Empêche la récursion infinie
    //@JsonProperty("sinister_id")         // Ajoute "sinisters" au JSON pour Postman
    @JsonIgnore

    private List<Sinister> sinisters;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = ContractStatus.PENDING; // Si aucun statut n'est spécifié, mettre "PENDING" par défaut
        }

        if (this.status == ContractStatus.ACTIVE) {
            this.startDate = LocalDate.now(); // La date de début est la date actuelle
            this.endDate = LocalDate.now().plusYears(1); // La date de fin est dans un an
        }
    }


}
