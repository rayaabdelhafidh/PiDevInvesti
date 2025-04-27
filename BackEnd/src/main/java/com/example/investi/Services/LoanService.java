package com.example.investi.Services;

import com.example.investi.Entities.Demand;
import com.example.investi.Entities.Loan;
import com.example.investi.Repositories.IDemandRepo;
import com.example.investi.Repositories.ILoanRepo;
import lombok.AllArgsConstructor;
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

