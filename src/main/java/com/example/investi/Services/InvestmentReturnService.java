package com.example.investi.Services;

import com.example.investi.Entities.InvestmentReturn;
import com.example.investi.Repositories.InvestmentReturnRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InvestmentReturnService implements IInvestmentReturnService<InvestmentReturn, Integer>{
    @Autowired
    private InvestmentReturnRepository investmentReturnRepository;
    @Override
    public InvestmentReturn save(InvestmentReturn investmentReturn) {
        return investmentReturnRepository.save(investmentReturn);
    }

    @Override
    public InvestmentReturn add(InvestmentReturn investmentReturn) {
        return investmentReturnRepository.save(investmentReturn);
    }

    @Override
    public Optional<InvestmentReturn> findById(Integer id) {
        return investmentReturnRepository.findById(id);
    }

    @Override
    public InvestmentReturn update(Integer id, InvestmentReturn newInvestmentReturn) {
        return investmentReturnRepository.findById(id)
                .map(existingInvestmentReturn -> {
                    existingInvestmentReturn.setRoiPercentage(newInvestmentReturn.getRoiPercentage());
                    existingInvestmentReturn.setPayoutDate(newInvestmentReturn.getPayoutDate());
                    return investmentReturnRepository.save(existingInvestmentReturn);
                }).orElse(null);
    }

    @Override
    public List<InvestmentReturn> findAll() {
        return investmentReturnRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        investmentReturnRepository.deleteById(id);

    }

    @Override
    public void delete(InvestmentReturn investmentReturn) {
        investmentReturnRepository.delete(investmentReturn);

    }
}
