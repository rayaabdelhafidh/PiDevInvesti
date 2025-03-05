package com.example.investi.Controllers;

import com.example.investi.Entities.InvestmentReturn;
import com.example.investi.Entities.Project;
import com.example.investi.Services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        Project savedProject = projectService.add(project);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer id, @RequestBody Project project) {
        Project updatedProject = projectService.update(id, project);
        return (updatedProject != null) ? ResponseEntity.ok(updatedProject) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            projectService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Project>> findById(@PathVariable int id) {
        Optional<Project> project = projectService.findById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return null;
    }
    @PutMapping("/dessignInvestToProject/{idProject}")
    public void dessignInvestmentToProject(@PathVariable("idProject")  int idProject)
    {
        Project project=projectService.desaffetcterInvestmentsToProject(idProject);
    }

    @GetMapping("/CalculateTotalInvestments/{idProject}")
    public BigDecimal calculateTotalInvestment(@PathVariable("idProject")  int idProject)
    {
        return projectService.calculateTotalInvestment(idProject);
    }

    @GetMapping("/getRelatedInvestmentReturns/{idProject}")
    public List<InvestmentReturn> getRelatedInvestmentReturns(@PathVariable("idProject")  int idProject)
    {
        return projectService.getRelatedInvestmentReturns(idProject);
    }

}
