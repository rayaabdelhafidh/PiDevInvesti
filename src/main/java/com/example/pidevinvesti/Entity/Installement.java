package com.example.pidevinvesti.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Installement {
    @Id
    private long InstId ;
    private float Amount;
    private Date DateEcheance ;
    private String Status ;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

}
