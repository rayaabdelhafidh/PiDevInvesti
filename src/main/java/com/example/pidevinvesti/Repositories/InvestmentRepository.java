package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {

}
