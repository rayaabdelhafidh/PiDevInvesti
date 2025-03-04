package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.StatusInvest;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {
    List<Investment> findAllByInvestId(int id);
    List<Investment> findByStatusInvest(StatusInvest status);
    @Query("SELECT i FROM Investment i JOIN i.project p")
    List<Investment> findAllInvestmentsWithProjects();
}
