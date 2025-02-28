package com.example.pidevinvesti.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int projectId;

    @JsonProperty("projectName")
    private String projectName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @JsonProperty("amountNeeded")

    private BigDecimal amountNeeded; // Total amount required for the project
    @JsonProperty("descriptionProject")

    private String descriptionProject;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus; // Enum for project status
    @JsonProperty("cumulInvest")

    private BigDecimal cumulInvest; // Total amount invested so far

    @JsonProperty("totalReturn")

    private BigDecimal totalReturn; // The total revenue generated from this project


    @OneToMany(mappedBy = "project",fetch =FetchType.EAGER)
    private List<Investment> investments;

    // Default constructor
    public Project() {}

    // Parameterized constructor
    public Project(String projectName, Date startDate, BigDecimal amountNeeded, String descriptionProject,
                   ProjectStatus projectStatus, BigDecimal cumulInvest, BigDecimal totalReturn, List<Investment> investments) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.amountNeeded = amountNeeded;
        this.descriptionProject = descriptionProject;
        this.projectStatus = projectStatus;
        this.cumulInvest = cumulInvest;
        this.totalReturn = totalReturn;
        this.investments = investments;
    }

    // Getters and Setters
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(BigDecimal amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public String getDescriptionProject() {
        return descriptionProject;
    }

    public void setDescriptionProject(String descriptionProject) {
        this.descriptionProject = descriptionProject;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public BigDecimal getCumulInvest() {
        return cumulInvest;
    }

    public void setCumulInvest(BigDecimal cumulInvest) {
        this.cumulInvest = cumulInvest;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }


    public List<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(List<Investment> investments) {
        this.investments = investments;
    }

    public BigDecimal calculateTotalRevenue() {
        if (investments == null || investments.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return investments.stream()
                .flatMap(investment -> investment.getTransactions().stream()) // Get all transactions
                .filter(transaction -> transaction.getTransactionType() == TransactionType.TRANSFERT) // Consider only income transactions
                .map(Transaction::getAmount) // Get the amounts
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all amounts
    }
    public void updateTotalReturn() {
        this.totalReturn = calculateTotalRevenue();
    }



}
