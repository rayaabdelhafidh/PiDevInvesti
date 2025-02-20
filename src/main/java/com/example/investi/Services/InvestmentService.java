package com.example.investi.Services;

import com.example.investi.Entities.Investment;
import com.example.investi.Repositories.InvestmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class InvestmentService implements IInvestmentService<Investment, Integer> {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Override
    public Investment save(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Investment add(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Optional<Investment> findById(Integer id) {
        return investmentRepository.findById(id);
    }

    @Override
    public Investment update(Integer id, Investment newInvestment) {
        return investmentRepository.findById(id)
                .map(existingInvestment -> {
                    existingInvestment.setAmount(newInvestment.getAmount());
                    existingInvestment.setInvestmentDate(newInvestment.getInvestmentDate());
                    existingInvestment.setDescriptionInvest(newInvestment.getDescriptionInvest());
                    existingInvestment.setStatusInvest(newInvestment.getStatusInvest());
                    return investmentRepository.save(existingInvestment);
                }).orElse(null);
    }

    @Override
    public List<Investment> findAll() {
        return investmentRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        investmentRepository.deleteById(id);
    }

    @Override
    public void delete(Investment investment) {
        investmentRepository.delete(investment);
    }
}
