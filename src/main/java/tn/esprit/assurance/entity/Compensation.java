package tn.esprit.assurance.entity;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compensationId;

    @ManyToOne
    @JoinColumn(name = "sinisterId")
    private Sinister sinister;

    private BigDecimal compensatedAmount;
    private LocalDate compensationDate;
    private String compensationMode;
    private long settlementDelay; // Time between sinister and compensation

    // Getters and Setters
}
