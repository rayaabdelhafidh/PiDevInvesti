package com.example.investi.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount; // Montant du paiement

    @Column(nullable = false)
    private LocalDateTime paymentDate; // Date et heure du paiement

    @ManyToOne
    @JoinColumn(name = "carte_bancaire_id", nullable = false)
    private CarteBancaire carteBancaire; // Référence à la carte bancaire utilisée


}