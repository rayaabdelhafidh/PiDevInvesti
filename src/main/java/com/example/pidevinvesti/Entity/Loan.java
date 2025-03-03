package com.example.pidevinvesti.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    @OneToOne
    private Demand demand ;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installement> installments;

    public long getLoanId() {
        return LoanId;
    }

    public void setLoanId(long loanId) {
        LoanId = loanId;
    }

    public Float getAmount() {
        return Amount;
    }

    public void setAmount(Float amount) {
        Amount = amount;
    }

    public Float getInterestRate() {
        return InterestRate;
    }

    public void setInterestRate(Float interestRate) {
        InterestRate = interestRate;
    }

    public Date getDateDebut() {
        return DateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        DateDebut = dateDebut;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public List<Installement> getInstallments() {
        return installments;
    }

    public void setInstallments(List<Installement> installments) {
        this.installments = installments;
    }
}
