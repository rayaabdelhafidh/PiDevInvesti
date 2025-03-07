package com.example.investi.Services;


import com.example.investi.Entities.Investor;
import com.example.investi.Repositories.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestorService implements IInvestorService<Investor, Long> {

    @Autowired
    private InvestorRepository investorRepository;

    public Investor getInvestorById(Long id) {
        return investorRepository.findById(id).orElse(null);
    }
}
