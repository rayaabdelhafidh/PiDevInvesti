package com.example.investi.Services;

import com.example.investi.Entities.Devise;
import com.example.investi.Repositories.DeviseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeviseService {


    private DeviseRepository deviseRepository;

    public double convertisseur(double montant, String deviseSource, String deviseDestination) {
        // Récupérer les devises correspondantes aux codes
        Devise deviseSourceTrouvee = deviseRepository.findBySymboleDevise(deviseSource);
        Devise deviseDestinationTrouvee = deviseRepository.findBySymboleDevise(deviseDestination);

        // Vérifier si les devises existent
        if (deviseSourceTrouvee == null || deviseDestinationTrouvee == null) {
            throw new IllegalArgumentException("Devise introuvable");
        }

        // Calculer le taux de change cumulé
        double tauxChangeCumule = deviseDestinationTrouvee.getTauxChange() / deviseSourceTrouvee.getTauxChange();

        // Appliquer le taux de change au montant
        return montant * tauxChangeCumule;
    }
}
