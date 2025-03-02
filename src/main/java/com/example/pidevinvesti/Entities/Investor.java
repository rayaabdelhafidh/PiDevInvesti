package com.example.pidevinvesti.Entities;
import com.example.pidevinvesti.Entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.*;
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

    @OneToMany(mappedBy = "investor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Investment> investments;

    @OneToOne(mappedBy = "investor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private Account account; // Each Investor has only one Account
}
