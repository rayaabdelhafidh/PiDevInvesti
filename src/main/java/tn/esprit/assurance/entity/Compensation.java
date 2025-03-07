package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Compensation {

    public enum CompensationStatus {
        EN_ATTENTE,
        PAYE
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("compensation_id")
    private Long compensationId;

    @ManyToOne
    @JoinColumn(name = "sinister_id")
    @JsonIgnoreProperties("compensations") // Évite la récursion infinie
    @JsonProperty("sinister")
    @JsonIgnore
    private Sinister sinister;

    @JsonProperty("compensated_amount")
    private double compensatedAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("compensation_date")
    private LocalDate compensationDate;


    @JsonProperty("settlement_delay")
    private long settlementDelay; // Temps entre le sinistre et la compensation
    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private CompensationStatus status;

    @PrePersist
    public void setDefaultStatus() {
        if (status == null) {
            status = CompensationStatus.EN_ATTENTE;
        }

    }

    public void calculerDureeRemboursement() {
        if (sinister != null && sinister.getSinisterDate() != null && compensationDate != null) {
            this.settlementDelay = java.time.temporal.ChronoUnit.DAYS.between(sinister.getSinisterDate(), compensationDate);
        }
    }


    }

