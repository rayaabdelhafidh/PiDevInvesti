package com.example.investi.Repositories;

import com.example.investi.Entities.InvestmentReturn;
import com.example.investi.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestmentReturnRepository extends JpaRepository<InvestmentReturn, Integer> {

    @Query("SELECT ir FROM InvestmentReturn ir JOIN ir.investment i JOIN i.project p")
    List<InvestmentReturn> findAllInvestmentReturnsWithInvestmentsAndProjects();
    List<InvestmentReturn> findByInvestment_Project(Project project);

}
