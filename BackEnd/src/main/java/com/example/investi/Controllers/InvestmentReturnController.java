package com.example.investi.Controllers;

import com.example.investi.Entities.InvestmentReturn;
import com.example.investi.Services.InvestmentReturnService;
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
    public ResponseEntity<InvestmentReturn> addInvestmentReturn(
            @RequestHeader("Authorization") String token,
            @RequestBody InvestmentReturn investmentReturn) {
        InvestmentReturn savedInvestmentReturn = investmentReturnService.add(investmentReturn);
        return ResponseEntity.ok(savedInvestmentReturn);
    }

    @GetMapping
    public ResponseEntity<List<InvestmentReturn>> getAllInvestmentReturns(
            @RequestHeader("Authorization") String token
            ) {
        List<InvestmentReturn> investmentReturns = investmentReturnService.findAll();
        return ResponseEntity.ok(investmentReturns);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<InvestmentReturn> updateInvestmentReturn(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id, @RequestBody InvestmentReturn investmentReturn) {
        InvestmentReturn updatedInvestmentReturn = investmentReturnService.update(id, investmentReturn);
        return (updatedInvestmentReturn != null) ? ResponseEntity.ok(updatedInvestmentReturn) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInvestmentReturn(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        Optional<InvestmentReturn> investmentReturn = investmentReturnService.findById(id);
        if (investmentReturn.isPresent()) {
            investmentReturnService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<InvestmentReturn>> findById(
            @RequestHeader("Authorization") String token,
            @PathVariable int id) {
        Optional<InvestmentReturn> investmentReturn = investmentReturnService.findById(id);
        if (investmentReturn != null) {
            return ResponseEntity.ok(investmentReturn);
        }
        return null;
    }
}
