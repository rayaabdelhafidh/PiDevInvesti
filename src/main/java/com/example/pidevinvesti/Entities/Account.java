package com.example.pidevinvesti.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Project project;

    @OneToOne
    @JoinColumn(name = "investor_id", unique = true) // Ensures an investor has only one account
    @JsonBackReference
    private Investor investor; // This links the account to an investor

    // Constructor with Client
    public Account(Client client) {
        this.client = client;
    }

    // Method to associate a project with the account
    public void setProject(Project project) {
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public double getDecouvertAutorise() {
        return decouvertAutorise;
    }

    public double getFraisMensuels() {
        return fraisMensuels;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public double getPlafondRetrait() {
        return plafondRetrait;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Project getProject() {
        return project;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClient(Client client) {
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

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}