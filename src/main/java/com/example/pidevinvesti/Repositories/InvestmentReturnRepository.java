package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.InvestmentReturn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentReturnRepository extends JpaRepository<InvestmentReturn, Integer> {

}
