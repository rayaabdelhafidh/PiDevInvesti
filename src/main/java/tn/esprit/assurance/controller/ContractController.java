package tn.esprit.assurance.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.assurance.entity.Contract;
import tn.esprit.assurance.services.IContractServices;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@AllArgsConstructor
@RequiredArgsConstructor
public class ContractController {
    @Autowired
    private IContractServices contractService;

    @PostMapping("/add/{clientId}")
    public Contract createContract(@PathVariable Long clientId, @RequestBody Contract contract) {
        return contractService.addContract(clientId, contract);
    }

    @GetMapping
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/{id}")
    public Contract getContractById(@PathVariable Long id) {
        return contractService.getContractById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    @PutMapping("/update/{id}")
    public Contract updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        return contractService.updateContract(id, contract);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
    }
}
