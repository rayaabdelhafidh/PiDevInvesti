package com.example.pidevinvesti.Controllers;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.Investor;
import com.example.pidevinvesti.Entities.Project;
import com.example.pidevinvesti.Repositories.InvestmentRepository;
import com.example.pidevinvesti.Repositories.InvestorRepository;
import com.example.pidevinvesti.Repositories.ProjectRepository;
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

