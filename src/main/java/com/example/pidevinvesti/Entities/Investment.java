package com.example.pidevinvesti.Entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Investment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int investId;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @Temporal(TemporalType.DATE)
    private Date investmentDate;
    @JsonProperty("descriptionInvest")
    private String descriptionInvest;

    @JsonProperty("statusInvest")
    @Enumerated(EnumType.STRING)
    private StatusInvest statusInvest;

    @Enumerated(EnumType.STRING)
    private ProgressInvestment investmentProgress;

    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    private Project project;

    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    private Investor investor;

    @OneToMany(mappedBy = "investment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<InvestmentReturn> investmentReturns;


    // Default constructor
    public Investment() {}

    // Constructor with all fields
    public Investment(int investId, BigDecimal amount, Date investmentDate, String descriptionInvest,
                      StatusInvest statusInvest, ProgressInvestment investmentProgress, Project project,
                      List<InvestmentReturn> investmentReturns) {
        this.investId = investId;
        this.amount = amount;
        this.investmentDate = investmentDate;
        this.descriptionInvest = descriptionInvest;
        this.statusInvest = statusInvest;
        this.investmentProgress = investmentProgress;
        this.project = project;
        this.investmentReturns = investmentReturns;
    }

    // Getters and Setters

    public int getInvestId() {
        return investId;
    }

    public void setInvestId(int investId) {
        this.investId = investId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getInvestmentDate() {
        return investmentDate;
    }

    public void setInvestmentDate(Date investmentDate) {
        this.investmentDate = investmentDate;
    }

    public String getDescriptionInvest() {
        return descriptionInvest;
    }

    public void setDescriptionInvest(String descriptionInvest) {
        this.descriptionInvest = descriptionInvest;
    }

    public StatusInvest getStatusInvest() {
        return statusInvest;
    }

    public void setStatusInvest(StatusInvest statusInvest) {
        this.statusInvest = statusInvest;
    }

    public ProgressInvestment getInvestmentProgress() {
        return investmentProgress;
    }

    public void setInvestmentProgress(ProgressInvestment investmentProgress) {
        this.investmentProgress = investmentProgress;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public List<InvestmentReturn> getInvestmentReturns() {
        return investmentReturns;
    }

    public void setInvestmentReturns(List<InvestmentReturn> investmentReturns) {
        this.investmentReturns = investmentReturns;
    }


}
