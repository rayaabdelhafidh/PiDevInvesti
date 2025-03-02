package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> {


    }
