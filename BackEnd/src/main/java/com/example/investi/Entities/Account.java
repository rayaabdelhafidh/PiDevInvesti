package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(unique = true)
    private String ethereumAddress;

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

    // Relation avec Project (si nécessaire)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    // Relation un-à-un avec CarteBancaire (chaque compte a une seule carte)
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("carteBancaire")
    private CarteBancaire carteBancaire;

    @OneToOne
    @JoinColumn(name = "investor_id", unique = true) // Ensures an investor has only one account
    private Investor investor; //

    // Constructor with Client
    public Account(Client client) {
        this.client = client;
        generateDefaultCard(); // Générer une carte par défaut lors de la création du compte
    }

    // Méthode pour associer un projet au compte
    public void setProject(Project project) {
        this.project = project;
    }

    // Méthode pour générer une carte par défaut lors de la création du compte
    @PostPersist
    protected void generateDefaultCard() {
        if (this.carteBancaire == null) { // Vérifier si aucune carte n'existe déjà
            CarteBancaire defaultCard = new CarteBancaire(this);
            defaultCard.generateCardDetails(); // Générer les détails de la carte
            defaultCard.setSpendingLimit(1000.0); // Définir un plafond de dépense par défaut
            this.carteBancaire = defaultCard; // Associer la carte au compte
        }
    }
}