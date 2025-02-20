package com.example.pidevinvesti.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Loan {
    @Id
    private long LoanId ;
    private Float Amount;
    private Float InterestRate;
    private Date DateDebut ;

    @OneToOne(mappedBy = "loan")
    private Demand demand ;
}
