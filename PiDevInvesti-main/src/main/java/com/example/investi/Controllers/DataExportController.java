package com.example.investi.Controllers;

import com.example.investi.Entities.Investment;
import com.example.investi.Entities.Investor;
import com.example.investi.Entities.Project;
import com.example.investi.Repositories.InvestmentRepository;
import com.example.investi.Repositories.InvestorRepository;
import com.example.investi.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataExportController {

        @Autowired
        private InvestmentRepository investmentRepo;
        @Autowired private InvestorRepository investorRepo;
        @Autowired private ProjectRepository projectRepo;

        @GetMapping("/investments")
        public List<Investment> getAllInvestments() {
            return investmentRepo.findAll();
        }

        @GetMapping("/investors")
        public List<Investor> getAllInvestors() {
            return investorRepo.findAll();
        }

        @GetMapping("/projects")
        public List<Project> getAllProjects() {
            return projectRepo.findAll();
        }
    }

