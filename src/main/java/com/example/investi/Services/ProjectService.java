package com.example.investi.Services;
import com.example.investi.Entities.Investment;
import com.example.investi.Entities.InvestmentReturn;
import com.example.investi.Entities.Project;
import com.example.investi.Repositories.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService implements IProjectService<Project, Integer> {

    @Autowired
    private ProjectRepository projectRepository;
    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project add(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> findById(Integer id) {
        return projectRepository.findById(id);    }

    @Override
    public Project update(Integer id, Project newProject) {
        BigDecimal totalInvestment = calculateTotalInvestment(id);
        newProject.setCumulInvest(totalInvestment);
        BigDecimal totalReturn = calculateTotalReturn(id);
        newProject.setTotalReturn(totalReturn);
        return projectRepository.findById(id)
                .map(existingPorject -> {
                    existingPorject.setDescriptionProject(newProject.getDescriptionProject());
                    existingPorject.setProjectName(newProject.getProjectName());
                    existingPorject.setAmountNeeded(newProject.getAmountNeeded());
                    existingPorject.setStartDate(newProject.getStartDate());
                    return projectRepository.save(existingPorject);
                }).orElse(null);
    }

    public BigDecimal calculateTotalReturn(int projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();

            return project.getInvestments()
                    .stream()
                    .flatMap(investment -> investment.getInvestmentReturns().stream()) // Get all investment returns
                    .map(InvestmentReturn::getTotalReturn) // Extract total return
                    .filter(returnValue -> returnValue != null) // Prevent null issues
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all returns
        }
        return BigDecimal.ZERO;
    }


    // Calculate total investment for a project
    public BigDecimal calculateTotalInvestment(int projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            return project.getInvestments()
                    .stream()
                    .map(Investment::getAmount) // Directly get BigDecimal amount
                    .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all amounts
        }
        return BigDecimal.ZERO;
    }

    // Update project investment total
    public void updateProjectInvestmentTotal(int projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            BigDecimal totalInvestment = calculateTotalInvestment(projectId);
            project.setCumulInvest(totalInvestment);
            projectRepository.save(project);
        }
    }
    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();    }

    @Override
    public void deleteById(Integer id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }
}
