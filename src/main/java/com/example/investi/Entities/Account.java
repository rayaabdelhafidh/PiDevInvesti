package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor // Generates a constructor for all fields
@NoArgsConstructor // Generates a no-argument constructor
@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Cascade the save operation to the Client entity
    @JoinColumn(name = "client_id", nullable = false)
    @JsonProperty("client") // Ensures proper mapping of client field
    private user client;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(user client) {
        this.client = client;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setDecouvertAutorise(double decouvertAutorise) {
        this.decouvertAutorise = decouvertAutorise;
    }

    public void setFraisMensuels(double fraisMensuels) {
        this.fraisMensuels = fraisMensuels;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public void setPlafondRetrait(double plafondRetrait) {
        this.plafondRetrait = plafondRetrait;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("balance")
    private double balance;
    @JsonProperty("decouvertAutorise")

    private double decouvertAutorise;
    @JsonProperty("fraisMensuels")

    private double fraisMensuels;

    @JsonProperty("tauxInteret")
    private double tauxInteret;

    @JsonProperty("plafondRetrait")
    private double plafondRetrait;



    @Enumerated(EnumType.STRING)
    @JsonProperty("status") // Ensures status is correctly mapped
    private AccountStatus status;



    // Constructor with Client
    public Account(user client) {
        this.client = client;
    }

}
