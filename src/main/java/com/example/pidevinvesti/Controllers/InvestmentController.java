package com.example.pidevinvesti.Controllers;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Services.InvestmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;


import java.util.List;

@RestController
@Slf4j
@RequestMapping("/investment")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

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
    @PutMapping("/invest/{id_user}/{amount}/{id_invest}")
    Investment invest(@PathVariable("id_user") int account_id, @PathVariable("amount") BigDecimal amount, @PathVariable("id_invest")Integer id_invest,@PathVariable("project_id")Integer project_id) {
        return investmentService.Invest(account_id,amount,id_invest,project_id);
    }

    @PutMapping("/accept/{id}")
    Investment AcceptInvestment(@PathVariable("id") Integer id){
        return investmentService.AcceptInvestment(id);
    }
    @PutMapping("/refuse/{id}")
    Investment RefuseInvestment(@PathVariable("id") Integer id){
        return investmentService.RefuseInvestment(id);
    }

    @PutMapping("/check")
    void CheckInvestment() {
        investmentService.Checkinvest();
    }
}