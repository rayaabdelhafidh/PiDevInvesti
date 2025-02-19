package tn.esprit.assurance.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.assurance.entity.Client;
import tn.esprit.assurance.entity.Contract;
import tn.esprit.assurance.entity.Sinister;
import tn.esprit.assurance.repositories.ClientRepository;
import tn.esprit.assurance.repositories.ContractRepository;

import java.util.List;
import java.util.Optional;

@Service

public class ContractServices implements IContractServices {
@Autowired
     private ContractRepository contractRepository;


    @Override
    public Contract addContract(Contract contract ) {

        return contractRepository.save(contract);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    @Override
    @Transactional
    public Contract updateContract( Contract updatedContract) {
        return contractRepository.save(updatedContract);

    }



    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }
}
