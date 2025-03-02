package com.example.pidevinvesti.Services;


import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.Investor;
import com.example.pidevinvesti.Repositories.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestorService implements IInvestorService<Investor, Long>{

    @Autowired
    private InvestorRepository investorRepository;

    public Investor getInvestorById(Long id) {
        return investorRepository.findById(id).orElse(null);
    }
}
