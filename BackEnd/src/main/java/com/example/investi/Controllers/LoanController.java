package com.example.investi.Controllers;

import com.example.investi.Entities.Loan;
import com.example.investi.Services.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Loan")
@AllArgsConstructor
public class LoanController {

    @Autowired
    ILoanService loanService;

 //   @PostMapping("/add")
  //  public Loan addLoan(@RequestBody Loan loan) {
    //    return loanService.addLoan(loan);
   // }

    @PutMapping("/update")
    public Loan updateLoan(@RequestBody Loan loan) {
        return loanService.updateLoan(loan);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
    }

    @GetMapping("/get/{id}")
    public Loan getLoan(@PathVariable Long id) {
        return loanService.getLoan(id);
    }

    @GetMapping("/all")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }
}
