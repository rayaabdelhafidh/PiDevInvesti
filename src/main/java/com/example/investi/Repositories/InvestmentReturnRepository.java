package com.example.investi.Repositories;

import com.example.investi.Entities.InvestmentReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentReturnRepository extends JpaRepository<InvestmentReturn, Integer> {

}
