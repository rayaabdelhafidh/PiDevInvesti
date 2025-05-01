package com.example.investi.Services;
import com.example.investi.Entities.Investment;
import com.example.investi.Entities.InvestmentReturn;
import com.example.investi.Entities.Project;
import com.example.investi.Repositories.InvestmentRepository;
import com.example.investi.Repositories.InvestmentReturnRepository;
import com.example.investi.Repositories.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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
        return projectRepository.findById(id);
    }

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
                    existingPorject.setHistoricalROI(newProject.getHistoricalROI());
                    existingPorject.setProjectStatus(newProject.getProjectStatus());
                    existingPorject.setRiskLevel(newProject.getRiskLevel());
                    existingPorject.setProjectDuration(newProject.getProjectDuration());

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
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }

    @Override
    public void updateProjectInvestmentTotal(Integer projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            BigDecimal totalInvestment = calculateTotalInvestment(projectId);
            project.setCumulInvest(totalInvestment);
            projectRepository.save(project);
        }
    }

    @Override
    public Project desaffetcterInvestmentsToProject(Integer idProject) {
        Project project = projectRepository.findById(idProject)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Set each investment's project to null
        for (Investment investment : project.getInvestments()) {
            investment.setProject(null);
            investmentRepository.save(investment); //  Update investment in DB
        }

        // Clear the list in the project
        project.getInvestments().clear();
        project.setCumulInvest(BigDecimal.ZERO); // Reset total investment

        return projectRepository.save(project);
    }

    public void calculateAndDistributeROI(Integer projectId) {
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

    @Override
    public BigDecimal calculateTotalInvestment(Integer projectId) {
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

    @Override
    public List<InvestmentReturn> getRelatedInvestmentReturns(Integer projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            // Flatten the list of InvestmentReturns
            return project.getInvestments().stream()
                    .flatMap(investment -> investment.getInvestmentReturns().stream()) // Flatten the InvestmentReturns
                    .collect(Collectors.toList()); // Collect the flattened results into a list
        }
        return null;
    }
    public Map<String, Object> getProjectAnalytics(int projectId) {
        Map<String, Object> analytics = new HashMap<>();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Récupérer les investissements du projet
        List<Investment> investments = investmentRepository.findByProject(project);

        // Récupérer les rendements des investissements liés à ce projet
        List<InvestmentReturn> returns = investmentReturnRepository.findByInvestment_Project(project);

        // Calculer le total des investissements
        BigDecimal totalInvested = investments.stream()
                .map(Investment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculer le total des rendements
        BigDecimal totalReturn = returns.stream()
                .map(InvestmentReturn::getTotalReturn)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ROI moyen
        BigDecimal avgROI = BigDecimal.ZERO;
        if(returns.size() > 0){
            avgROI = returns.stream()
                    .map(InvestmentReturn::getRoiPercentage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(returns.size()), RoundingMode.HALF_UP);
        }

        analytics.put("totalInvested", totalInvested);
        analytics.put("totalReturn", totalReturn);
        analytics.put("avgROI", avgROI);

        analytics.put("investmentTrends", investments.stream()
                .collect(Collectors.groupingBy(
                        inv -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(inv.getInvestmentDate());
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is 0-based
                            return year + "-" + (month < 10 ? "0" + month : month);  // Year-Month format
                        },
                        Collectors.summingDouble(inv -> inv.getAmount().doubleValue())
                ))
        );

        analytics.put("returnTrends", returns.stream()
                .collect(Collectors.groupingBy(
                        ret -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(ret.getPayoutDate());
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is 0-based
                            return year + "-" + (month < 10 ? "0" + month : month);  // Year-Month format
                        },
                        Collectors.summingDouble(ret -> ret.getTotalReturn().doubleValue())
                ))
        );
        return analytics;
    }
}
