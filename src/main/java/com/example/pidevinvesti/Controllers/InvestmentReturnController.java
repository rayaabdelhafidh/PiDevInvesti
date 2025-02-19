package com.example.pidevinvesti.Controllers;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.InvestmentReturn;
import com.example.pidevinvesti.Services.InvestmentReturnService;
import com.example.pidevinvesti.Services.InvestmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/investmentReturn")
public class InvestmentReturnController {
    @Autowired
    private InvestmentReturnService investmentReturnService;

    @PostMapping("/add")
    public ResponseEntity<InvestmentReturn> addInvestmentReturn(@RequestBody InvestmentReturn investmentReturn) {
        InvestmentReturn savedInvestmentReturn = investmentReturnService.add(investmentReturn);
        return ResponseEntity.ok(savedInvestmentReturn);
    }

    @GetMapping
    public ResponseEntity<List<InvestmentReturn>> getAllInvestmentReturns() {
        List<InvestmentReturn> investmentReturns = investmentReturnService.findAll();
        return ResponseEntity.ok(investmentReturns);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InvestmentReturn> updateInvestmentReturn(@PathVariable Integer id, @RequestBody InvestmentReturn investmentReturn) {
        InvestmentReturn updatedInvestmentReturn = investmentReturnService.update(id, investmentReturn);
        return (updatedInvestmentReturn != null) ? ResponseEntity.ok(updatedInvestmentReturn) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInvestmentReturn(@PathVariable Integer id) {
        Optional<InvestmentReturn> investmentReturn = investmentReturnService.findById(id);
        if (investmentReturn.isPresent()) {
            investmentReturnService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<InvestmentReturn>> findById(@PathVariable int id) {
        Optional<InvestmentReturn> investmentReturn = investmentReturnService.findById(id);
        if (investmentReturn != null) {
            return ResponseEntity.ok(investmentReturn);
        }
        return null;
    }
}
