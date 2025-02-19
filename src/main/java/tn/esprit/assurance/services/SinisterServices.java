package tn.esprit.assurance.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.assurance.entity.Sinister;
import tn.esprit.assurance.repositories.SinisterRepository;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class SinisterServices implements ISinisterServices{

@Autowired
       private SinisterRepository sinisterRepository;


        // Ajouter un sinistre
        public Sinister addSinister(Sinister sinister) {
            return sinisterRepository.save(sinister);
        }

        // Récupérer tous les sinistres
        public List<Sinister> getAllSinisters() {
            return sinisterRepository.findAll();
        }

        // Récupérer un sinistre par ID
        public Optional<Sinister> getSinisterById(Long id) {
            return sinisterRepository.findById(id);
        }

    @Override
    public Sinister updateSinister(Long id, Sinister sinisterDetails) {
        return null;
    }

    // Modifier un sinistre


        // Supprimer un sinistre
        public void deleteSinister(Long id) {
            sinisterRepository.deleteById(id);
        }
    }

