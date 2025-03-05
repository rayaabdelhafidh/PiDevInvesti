package com.example.investi.Services;

import com.example.investi.Entities.Sinister;

import java.util.List;
import java.util.Optional;

public interface ISinisterServices {
    Sinister addSinister(Sinister sinister);  // Ajouter un sinistre

    List<Sinister> getAllSinisters();  // Récupérer tous les sinistres

    Optional<Sinister> getSinisterById(Long id);  // Récupérer un sinistre par son ID

    Sinister updateSinister(Long id, Sinister sinisterDetails);  // Modifier un sinistre

    void deleteSinister(Long id);  // Supprimer un sinistre


}



