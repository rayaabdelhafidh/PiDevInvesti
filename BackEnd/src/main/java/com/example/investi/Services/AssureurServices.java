package com.example.investi.Services;

import com.example.investi.Entities.Assureur;
import com.example.investi.Repositories.AssureurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssureurServices {
@Autowired
    AssureurRepository assureurRepository;

public Assureur AddAssureur(Assureur assureur)
{
    return assureurRepository.save(assureur);

}




    public Assureur updateAssureur(Assureur assureur) {
        // Vérifier si l'assureur existe dans la base de données
        Assureur existingAssureur = assureurRepository.findById(assureur.getId())
                .orElseThrow(() -> new IllegalArgumentException("Assureur non trouvé avec l'ID : " + assureur.getId()));

        // Mettre à jour les champs modifiables
        if (assureur.getFirstName() != null) {
            existingAssureur.setFirstName(assureur.getFirstName());
        }
        if (assureur.getLastName() != null) {
            existingAssureur.setLastName(assureur.getLastName());
        }
        if (assureur.getEmail() != null) {
            existingAssureur.setEmail(assureur.getEmail());
        }
        if (assureur.getAdresse() != null) {
            existingAssureur.setAdresse(assureur.getAdresse());
        }
        if (assureur.getPhonenumber() != null) {
            existingAssureur.setPhonenumber(assureur.getPhonenumber());
        }

        // Sauvegarder les modifications dans la base de données
        return assureurRepository.save(existingAssureur);
    }


    public void deleteAssureur(Long id) {
        assureurRepository.deleteById(id);
    }


    public Assureur getAssureurById(Long id) {
        return assureurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assureur non trouvé avec l'ID : " + id));
    }


    public List<Assureur> getAllAssureurs() {
        return assureurRepository.findAll();
    }
   public Assureur GetAssureurById(Long id)
   {
       return assureurRepository.findById(id).get();
   }

}
