package tn.esprit.assurance.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/add")
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        return ResponseEntity.ok(contractService.addContract(contract));
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContractById(@PathVariable Long id) {
        return contractService.getContractById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<Contract> updateContract( @RequestBody Contract contract) {
        Contract updatedContract = contractService.updateContract(contract);
        return ResponseEntity.ok(updatedContract);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
