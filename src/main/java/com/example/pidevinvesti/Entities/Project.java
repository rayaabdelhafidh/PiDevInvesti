package com.example.pidevinvesti.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonProperty("projectDuration")

    private int projectDuration;

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

    // New fields for recommendation system
    @JsonProperty("sector")
    private String sector; // Industry sector

    @Enumerated(EnumType.STRING)
    @JsonProperty("riskLevel")
    private RiskProfile riskLevel; // LOW, MEDIUM, HIGH

    @JsonProperty("historicalROI")
    private BigDecimal historicalROI; // Average ROI for the project

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    private Account account; // Each Project has one Account
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
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


        public void updateTotalReturn() {
        this.totalReturn = cumulInvest;
    }

    public int getProjectDuration() {
        return projectDuration;
    }

    public void setProjectDuration(int projectDuration) {
        this.projectDuration = projectDuration;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public RiskProfile getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskProfile riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getHistoricalROI() {
        return historicalROI;
    }

    public void setHistoricalROI(BigDecimal historicalROI) {
        this.historicalROI = historicalROI;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
