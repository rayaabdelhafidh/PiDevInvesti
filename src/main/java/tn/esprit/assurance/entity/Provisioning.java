package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Provisioning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long provisioningId;

    @ManyToOne
    @JoinColumn(name = "contractId")
    @JsonIgnore

    private Contract contract;

    private BigDecimal psap;
    private BigDecimal bestEstimate;
    private BigDecimal riskMargin;
    private LocalDate calculationDate;
   /* @OneToMany(mappedBy = "provisioning")
    private List<Sinister> sinistres; // Les sinistres liés au provisionnement*/

    // Getters and Setters
}

