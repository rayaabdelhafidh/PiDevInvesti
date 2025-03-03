package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.Loan;
import com.example.pidevinvesti.Repository.IDemandRepo;
import com.example.pidevinvesti.Repository.ILoanRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class LoanService implements ILoanService {

    @Autowired
    private ILoanRepo loanRepository;
    @Autowired
    private IDemandRepo demandRepo;

    @Override
    public Loan addLoan(Loan loan , Long demandId) {
       Demand demand = demandRepo.findById(demandId).get();
       loan.setDemand(demand);
        return loanRepository.save(loan);
    }

    @Override
    public Loan updateLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    @Override
    public Loan getLoan(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}

