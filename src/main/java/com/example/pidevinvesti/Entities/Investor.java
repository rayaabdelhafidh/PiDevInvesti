package com.example.pidevinvesti.Entities;
import com.example.pidevinvesti.Entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
