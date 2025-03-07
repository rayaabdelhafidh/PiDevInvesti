package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Investor extends User {

    @JsonProperty("investamount")
    private double investamount;
    @JsonProperty("description")
    private String description;
    @Enumerated(EnumType.STRING)
    @JsonProperty("investorStatus")
    private InvestorStatus investorStatus;
    @JsonProperty("investmentdate")
    private LocalDateTime investmentdate;
    @Enumerated(EnumType.STRING)
    @JsonProperty("riskTolerance")
    private RiskProfile riskTolerance; // LOW, MEDIUM, HIGH

    @JsonProperty("preferredSectors")
    private String preferredSectors; // Comma-separated list of sectors

    @OneToMany(mappedBy = "investor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Investment> investments;

    @OneToOne(mappedBy = "investor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account; // Each Investor has only one Account
}
