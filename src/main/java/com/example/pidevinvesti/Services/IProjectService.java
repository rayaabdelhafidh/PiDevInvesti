package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.Project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProjectService <Project,ID>{
    Project save(Project entity);

    Project add (Project entity);
    BigDecimal calculateTotalInvestment(ID id) ;


    Optional<Project> findById(ID id);

    Project update (ID id, Project entity);
    List<Project> findAll();

    Project desaffetcterInvestmentsToProject(ID id);

    void deleteById(ID id);

    void delete(Project entity);
    void updateProjectInvestmentTotal(ID id);
    void calculateAndDistributeROI(ID id);
}
