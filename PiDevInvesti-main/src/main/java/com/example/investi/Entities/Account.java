package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonProperty("client")
    private Client client;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

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
    @JsonProperty("status")
    private AccountStatus status;

    // New field for Project accounts
    @OneToOne(cascade = CascadeType.ALL) // Cascade save operations to the Project entity
    @JoinColumn(name = "project_id", nullable = true) // Nullable because not all accounts have a project
    private Project project;

    @OneToOne
    @JoinColumn(name = "investor_id", unique = true) // Ensures an investor has only one account
    private Investor investor; // This links the account to an investor

    // Constructor with Client
    public Account(Client client) {
        this.client = client;
    }

    // Method to associate a project with the account
    public void setProject(Project project) {
        this.project = project;
    }
}