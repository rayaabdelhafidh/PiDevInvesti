package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Investment;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {
    List<Investment> findAllByInvestId(int id);

}
