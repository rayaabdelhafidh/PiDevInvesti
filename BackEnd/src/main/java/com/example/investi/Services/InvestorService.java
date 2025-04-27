package com.example.investi.Services;

import com.example.investi.Entities.Investor;
import com.example.investi.Repositories.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestorService implements IInvestorService<Investor, Long> {

    @Autowired
    private InvestorRepository investorRepository;

    // Create or Update Investor
    public Investor saveInvestor(Investor investor) {
        return investorRepository.save(investor);
    }

    // Get Investor by ID
    public Investor getInvestorById(Long id) {
        return investorRepository.findById(id).orElse(null);
    }

    // Get All Investors
    public List<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

    // Update Investor
    public Investor updateInvestor(Investor investorDetails) {
        Optional<Investor> optionalInvestor = investorRepository.findById(investorDetails.getId());
        if (optionalInvestor.isPresent()) {
            Investor investor = optionalInvestor.get();
            investor.setFirstName(investorDetails.getFirstName()); // Exemple de mise à jour d'un champ
            investor.setEmail(investorDetails.getEmail());
            investor.setPhonenumber(investorDetails.getPhonenumber());
            return investorRepository.save(investor);
        }
        return null;
    }

    public Investor addInvestor(Investor investor) {
        return saveInvestor(investor);
    }
    // Delete Investor
    public boolean deleteInvestor(Long id) {
        if (investorRepository.existsById(id)) {
            investorRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Investor GetInvestorById(Long id) {
        return getInvestorById(id);
    }
}