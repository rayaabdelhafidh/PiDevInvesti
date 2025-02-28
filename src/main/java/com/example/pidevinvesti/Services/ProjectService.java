package com.example.pidevinvesti.Services;
import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.InvestmentReturn;
import com.example.pidevinvesti.Entities.Project;
import com.example.pidevinvesti.Repositories.InvestmentRepository;
import com.example.pidevinvesti.Repositories.InvestmentReturnRepository;
import com.example.pidevinvesti.Repositories.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService implements IProjectService<Project, Integer> {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private InvestmentReturnRepository investmentReturnRepository;
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


    @Override
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

@Override
    public Project affetcterInvestmentsToProject(List<Integer> idInvest, int idProject){
        Project project=projectRepository.findById(idProject).orElse(null);
        List<Investment> investments = investmentRepository.findAllById(idInvest);
        project.setInvestments(investments);
        projectRepository.save(project);
        return project;
    }
    @Override
    public Project desaffetcterInvestmentsToProject(int idProject){
        Project project=projectRepository.findById(idProject).orElse(null);
        project.setInvestments(null);
        projectRepository.save(project);
        return project;
    }

    public void calculateAndDistributeROI(int projectId) {
        // Get project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Update project total revenue
        project.updateTotalReturn();
        projectRepository.save(project);

        // Get total investments in the project
        BigDecimal totalInvestmentAmount = project.getCumulInvest();
        if (totalInvestmentAmount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("No investments in this project");
        }

        // Calculate and distribute returns to investors
        for (Investment investment : project.getInvestments()) {
            BigDecimal investorShare = investment.getAmount()
                    .divide(totalInvestmentAmount, RoundingMode.HALF_UP)
                    .multiply(project.getTotalReturn());

            BigDecimal roiPercentage = investorShare
                    .subtract(investment.getAmount())
                    .divide(investment.getAmount(), RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            InvestmentReturn investmentReturn = new InvestmentReturn();
            investmentReturn.setInvestment(investment);
            investmentReturn.setTotalReturn(investorShare);
            investmentReturn.setRoiPercentage(roiPercentage);
            investmentReturn.setPayoutDate(new Date());

            investmentReturnRepository.save(investmentReturn);
        }
    }
}
