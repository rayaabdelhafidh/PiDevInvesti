package com.example.investi.Controllers;

import com.example.investi.Entities.CarteBancaire;
import com.example.investi.Services.CarteBancaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cartes")
public class CarteBancaireController {

    @Autowired
    private CarteBancaireService carteBancaireService;


    @PostMapping("/{carteId}/block")
    public void blockCarteBancaire(@PathVariable Long carteId) {
        carteBancaireService.blockCarteBancaire(carteId);
    }


    @PostMapping("/{carteId}/unblock")
    public void unblockCarteBancaire(@PathVariable Long carteId) {
        carteBancaireService.unblockCarteBancaire(carteId);
    }


    @PutMapping("/{carteId}/update-expiry-date")
    public CarteBancaire updateExpiryDate(
            @PathVariable Long carteId,
            @RequestParam LocalDate newExpiryDate) {
        CarteBancaire updatedCarte = carteBancaireService.updateExpiryDate(carteId, newExpiryDate);
        return updatedCarte;
    }


    @PutMapping("/{carteId}/update-limit")
    public CarteBancaire updateLimit(
            @PathVariable Long carteId,
            @RequestParam double newLimit) {
        CarteBancaire updatedCarte = carteBancaireService.updateLimit(carteId, newLimit);
        return updatedCarte;
    }

    @DeleteMapping("/{carteId}")
    public void deleteCarteBancaire(@PathVariable Long carteId) {
        carteBancaireService.deleteCarteBancaire(carteId);
    }
    @PostMapping("/{carteId}/pay")
    public ResponseEntity<String> makePayment(
            @PathVariable Long carteId,
            @RequestParam double amount) {

        boolean paymentSuccess = carteBancaireService.makePayment(carteId, amount);

        if (paymentSuccess) {
            return ResponseEntity.ok("Paiement réussi !");
        } else {
            return ResponseEntity.badRequest().body("Échec du paiement.");
        }
    }
    @GetMapping("/{carteId}")
    public CarteBancaire getCarteBancaireDetails(@PathVariable Long carteId) {
        CarteBancaire carteBancaire = carteBancaireService.getCarteBancaireDetails(carteId);
        return carteBancaire;
    }
}