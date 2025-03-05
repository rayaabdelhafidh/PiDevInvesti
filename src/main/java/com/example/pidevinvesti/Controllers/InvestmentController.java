package com.example.pidevinvesti.Controllers;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.StatusInvest;
import com.example.pidevinvesti.Entities.Transaction;
import com.example.pidevinvesti.Entities.TransactionType;
import com.example.pidevinvesti.Repositories.InvestmentRepository;
import com.example.pidevinvesti.Services.InvestmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;


import java.util.List;

@RestController
@Slf4j
@RequestMapping("/investment")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private InvestmentRepository investmentRepository;

    @PostMapping("/add")
    public ResponseEntity<Investment> addInvestment(@RequestBody Investment investment) {
        Investment savedInvestment = investmentService.add(investment);
        return ResponseEntity.ok(savedInvestment);
    }

    @GetMapping
    public ResponseEntity<List<Investment>> getAllInvestments() {
        List<Investment> investments = investmentService.findAll();
        return ResponseEntity.ok(investments);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Investment> updateInvestment(@PathVariable Integer id, @RequestBody Investment investment) {
        Investment updatedInvestment = investmentService.update(id, investment);
        return (updatedInvestment != null) ? ResponseEntity.ok(updatedInvestment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public List<Investment> getInvestmentsByStatus(@PathVariable("status") StatusInvest status) {
        return investmentService.findByStatus(status);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Integer id) {
        Optional<Investment> investment = investmentService.findById(id);
        if (investment.isPresent()) {
            investmentService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Investment>> findById(@PathVariable int id) {
        Optional<Investment> investment = investmentService.findById(id);
        if (investment != null) {
            return ResponseEntity.ok(investment);
        }
        return null;
    }

    @PutMapping("/invest/{id_user}/{amount}/{project_id}")
    Investment invest(@PathVariable("id_user") long id_user, @PathVariable("amount") BigDecimal amount, @PathVariable("project_id") Integer project_id) {
        return investmentService.Invest(id_user, amount, project_id);
    }

    @PutMapping("/accept/{id}")
    Investment AcceptInvestment(@PathVariable("id") Integer id) {
        return investmentService.AcceptInvestment(id);
    }

    @PutMapping("/refuse/{id}")
    Investment RefuseInvestment(@PathVariable("id") Integer id) {
        return investmentService.RefuseInvestment(id);
    }

    @PutMapping("/check")
    void CheckInvestment() {
        investmentService.Checkinvest();
    }

    @GetMapping("/data")
    public ResponseEntity<List<Map<String, Object>>> getInvestmentData() {
        return ResponseEntity.ok(investmentService.getInvestmentData());
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<String> returnInvestment(@PathVariable("id") Integer id
    ) {
        try {
            investmentService.ReturnInvestment(id);
            return ResponseEntity.ok("Investment return processed successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
