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
public class Installement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long InstId ;
    private float Amount;
    private Date DateEcheance ;
    private String Status ;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public long getInstId() {
        return InstId;
    }

    public void setInstId(long instId) {
        InstId = instId;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public Date getDateEcheance() {
        return DateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        DateEcheance = dateEcheance;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
