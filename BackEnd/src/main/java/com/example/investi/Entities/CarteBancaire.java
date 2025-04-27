package com.example.investi.Entities;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.CarteStatus;
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
public class CarteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("cardNumber")
    @Column(unique = true, length = 16)
    private String cardNumber; // Numéro de la carte (16 chiffres)

    @JsonProperty("cvv")
    @Column(length = 3)
    private String cvv; // Code de sécurité CVV (3 chiffres)

    @JsonProperty("expiryDate")
    private LocalDate expiryDate; // Date d'expiration

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private CarteStatus status; // Statut de la carte (ACTIVE, BLOCKED, EXPIRED)

    @JsonProperty("spendingLimit") // Renommage du champ "limit" en "spendingLimit"
    private double spendingLimit; // Plafond de dépense

    // Relation un-à-un avec Account (une carte est liée à un seul compte)
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Constructeur avec Account
    public CarteBancaire(Account account) {
        this.account = account;
    }

    // Méthode pour générer automatiquement le numéro de carte et le CVV
    public void generateCardDetails() {
        this.cardNumber = generateRandomCardNumber();
        this.cvv = generateRandomCVV();
        this.expiryDate = LocalDate.now().plusYears(5); // Expiration dans 5 ans par défaut
        this.status = CarteStatus.ACTIVE; // Statut initial : ACTIVE
    }

    // Génération aléatoire du numéro de carte (16 chiffres)
    private String generateRandomCardNumber() {
        return String.format("%016d", (long) (Math.random() * 1_0000_0000_0000_0000L));
    }

    // Génération aléatoire du CVV (3 chiffres)
    private String generateRandomCVV() {
        return String.format("%03d", (int) (Math.random() * 1000));
    }

    // Méthode pour effectuer un paiement en utilisant le solde du compte
    public boolean makePayment(double amount) {
        if (this.account != null && this.account.getBalance() >= amount && amount <= this.spendingLimit) {
            this.account.setBalance(this.account.getBalance() - amount); // Déduire le montant du solde du compte
            return true; // Paiement réussi
        }
        return false; // Insuffisance de fonds ou dépassement du plafond
    }
}