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

public class Contract {
    public enum InsuranceType {
        HEALTH,  // Assurance Santé
        CREDIT   // Assurance Crédit
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @JsonProperty("contract_id") // Convertit contractId en "contract_id" dans JSON
    private long contractId;

    @ManyToOne
    @JoinColumn(name = "client_id")
      // Vérifie cette annotation
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
    private BigDecimal insuredAmount;

    @JsonProperty("annual_premium")
    private BigDecimal annualPremium;

    @JsonProperty("status")
    private String status;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("contract")  // Empêche la récursion infinie
    //@JsonProperty("sinister_id")         // Ajoute "sinisters" au JSON pour Postman
    private List<Sinister> sinisters;


}
