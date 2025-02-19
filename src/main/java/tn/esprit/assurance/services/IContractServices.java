package tn.esprit.assurance.services;

import tn.esprit.assurance.entity.Contract;

import java.util.List;
import java.util.Optional;


public interface IContractServices {
    Contract addContract(Contract contract);  // Ajouter un contrat

    List<Contract> getAllContracts();  // Récupérer tous les contrats

    Optional<Contract> getContractById(Long id);  // Récupérer un contrat par ID

    Contract updateContract(Contract contractDetails);  // Modifier un contrat

    void deleteContract(Long id);
}
