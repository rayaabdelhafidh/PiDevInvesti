package com.example.pidevinvesti.Entities;
import com.example.pidevinvesti.Entities.User;
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
    @OneToMany(mappedBy = "investor",fetch = FetchType.EAGER)
    private List<Investment> investments;
}
