package com.example.investi.Services;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.CarteBancaire;
import com.example.investi.Entities.CarteStatus;
import com.example.investi.Entities.Payment;
import com.example.investi.Repositories.AccountRepository;
import com.example.investi.Repositories.CartRepository;
import com.example.investi.Repositories.IpaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CarteBancaireService {

    @Autowired
    private CartRepository carteBancaireRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IpaymentRepository paymentRepository;
    public CarteBancaire createCarteBancaire(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Compte non trouvé"));

        // Vérifier si une carte existe déjà pour ce compte
        if (account.getCarteBancaire() != null) {
            // Return null instead of throwing an exception
            return null;
        }

        // Créer et associer une nouvelle carte bancaire au compte
        CarteBancaire carteBancaire = new CarteBancaire(account);
        carteBancaire.generateCardDetails(); // Générer les détails de la carte
        carteBancaire.setSpendingLimit(1000.0); // Définir un plafond de dépense par défaut

        // Sauvegarder la carte en base de données
        return carteBancaireRepository.save(carteBancaire);
    }



    public boolean makePayment(Long carteId, double amount) {
        // Récupérer la carte bancaire
        Payment payment = new Payment() ;

        CarteBancaire carteBancaire = carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));

        // Vérifier si la carte est active
        if (carteBancaire.getStatus() != CarteStatus.ACTIVE) {
            throw new IllegalStateException("La carte bancaire est bloquée ou expirée.");
        }

        // Récupérer le compte associé à la carte
        Account account = carteBancaire.getAccount();
        if (account == null) {
            throw new IllegalStateException("Aucun compte associé à cette carte bancaire.");
        }

        // Vérifier si le solde du compte est suffisant
        if (account.getBalance() < amount) {
            throw new IllegalStateException("Solde insuffisant pour effectuer ce paiement.");
        }

        // Vérifier si le montant ne dépasse pas le plafond de dépense
        if (amount > carteBancaire.getSpendingLimit()) {
            throw new IllegalStateException("Le montant dépasse le plafond de dépense autorisé.");
        }

        // Déduire le montant du solde du compte
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        LocalDateTime L1= new Date().toInstant()
         .atZone(ZoneId.of("Africa/Tunis")) // Specify a time zone
         .toLocalDateTime();

     payment.setPaymentDate(L1);
     payment.setCarteBancaire(carteBancaire);
     payment.setAmount(amount);

        // Enregistrer le paiement dans la base de données
        paymentRepository.save(payment);

        return true; // Paiement réussi
    }


    public void blockCarteBancaire(Long carteId) {
        CarteBancaire carteBancaire = carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));

        // Vérifier si la carte est déjà bloquée
        if (carteBancaire.getStatus() == CarteStatus.BLOCKED) {
            throw new IllegalStateException("La carte bancaire est déjà bloquée.");
        }

        // Bloquer la carte
        carteBancaire.setStatus(CarteStatus.BLOCKED);
        carteBancaireRepository.save(carteBancaire);
    }
    public void deleteCarteBancaire(Long carteId) {
        // Vérifier si la carte existe
        CarteBancaire carteBancaire = carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));

        // Supprimer la carte
        carteBancaireRepository.delete(carteBancaire);
    }

    public void unblockCarteBancaire(Long carteId) {
        CarteBancaire carteBancaire = carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));

        // Vérifier si la carte est déjà active
        if (carteBancaire.getStatus() == CarteStatus.ACTIVE) {
            throw new IllegalStateException("La carte bancaire est déjà active.");
        }

        // Débloquer la carte
        carteBancaire.setStatus(CarteStatus.ACTIVE);
        carteBancaireRepository.save(carteBancaire);
    }


    public CarteBancaire getCarteBancaireDetails(Long carteId) {
        return carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));
    }


    public CarteBancaire updateExpiryDate(Long carteId, LocalDate newExpiryDate) {
        CarteBancaire carteBancaire = carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));

        // Vérifier si la nouvelle date d'expiration est valide
        if (newExpiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date d'expiration doit être dans le futur.");
        }

        // Mettre à jour la date d'expiration
        carteBancaire.setExpiryDate(newExpiryDate);
       return  carteBancaireRepository.save(carteBancaire);
    }


    public CarteBancaire updateLimit(Long carteId, double newLimit) {
        CarteBancaire carteBancaire = carteBancaireRepository.findById(carteId)
                .orElseThrow(() -> new IllegalArgumentException("Carte bancaire non trouvée"));

        // Vérifier si le nouveau plafond est valide
        if (newLimit <= 0) {
            throw new IllegalArgumentException("Le plafond de dépense doit être supérieur à zéro.");
        }

        // Mettre à jour le plafond de dépense
        carteBancaire.setSpendingLimit(newLimit);
        return carteBancaireRepository.save(carteBancaire);
    }
}