package com.example.investi.Repositories;

import com.example.investi.Entities.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> {


    }
