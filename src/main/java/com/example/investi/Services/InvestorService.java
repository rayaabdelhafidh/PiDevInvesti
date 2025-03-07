package com.example.investi.Services;

import com.example.investi.Entities.Investor;
import com.example.investi.Repositories.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestorService {

    @Autowired
    private InvestorRepository investorRepository;


    public Investor AddInvestor(Investor investor) {
        return investorRepository.save(investor);
    }


    public Investor UpdateInvestor(Investor investor) {
        Optional<Investor> existingInvestor = investorRepository.findById(investor.getId());
        if (existingInvestor.isEmpty()) {
            throw new IllegalArgumentException("Investor not found with ID: " + investor.getId());
        }

        Investor updatedInvestor = existingInvestor.get();
        if (investor.getFirstName() != null) {
            updatedInvestor.setFirstName(investor.getFirstName());
        }
        if (investor.getLastName() != null) {
            updatedInvestor.setLastName(investor.getLastName());
        }
        if (investor.getEmail() != null) {
            updatedInvestor.setEmail(investor.getEmail());
        }
        if (investor.getPassword() != null) {
            updatedInvestor.setPassword(investor.getPassword());
        }
        if (investor.getAdresse() != null) {
            updatedInvestor.setAdresse(investor.getAdresse());
        }
        if (investor.getPhonenumber() != null) {
            updatedInvestor.setPhonenumber(investor.getPhonenumber());
        }
        if (investor.getInvestamount() != 0) {
            updatedInvestor.setInvestamount(investor.getInvestamount());
        }
        if (investor.getDescription() != null) {
            updatedInvestor.setDescription(investor.getDescription());
        }
        if (investor.getInvestorStatus() != null) {
            updatedInvestor.setInvestorStatus(investor.getInvestorStatus());
        }
        if (investor.getInvestmentdate() != null) {
            updatedInvestor.setInvestmentdate(investor.getInvestmentdate());
        }

        return investorRepository.save(updatedInvestor);
    }

    /**
     * Delete an investor by ID.
     *
     * @param id The ID of the investor to delete.
     */
    public void DeleteInvestor(Long id) {
        investorRepository.deleteById(id);
    }


    public Investor GetInvestorById(Long id) {
        return investorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Investor not found with ID: " + id));
    }


    public List<Investor> GetAllInvestors() {
        return investorRepository.findAll();
    }
}