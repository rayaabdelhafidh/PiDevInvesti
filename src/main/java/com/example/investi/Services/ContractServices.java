package com.example.investi.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.investi.Entities.Contract;
import com.example.investi.Repositories.ContractRepository;

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
