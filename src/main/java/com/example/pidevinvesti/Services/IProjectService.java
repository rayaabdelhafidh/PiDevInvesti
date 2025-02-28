package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.Project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IProjectService <Project,ID>{
    Project save(Project entity);

    Project add (Project entity);
    public BigDecimal calculateTotalInvestment(int projectId) ;

    public Project affetcterInvestmentsToProject(List<Integer> idInvest, int idProject);

    Optional<Project> findById(ID id);

    Project update (ID id, Project entity);
    List<Project> findAll();

    public Project desaffetcterInvestmentsToProject(int idProject);


        void deleteById(ID id);

    void delete(Project entity);
}
