package com.example.investi.Repositories;

import com.example.investi.Entities.Investment;
import com.example.investi.Entities.Project;
import com.example.investi.Entities.StatusInvest;
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
    List<Investment> findByProject(Project project);
}
