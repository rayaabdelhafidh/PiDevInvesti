package com.example.investi.Repositories;

import com.example.investi.Entities.StockInvestment;
import com.example.investi.Services.IStockService;
import com.example.investi.Services.StockService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInvestmentRepository extends JpaRepository<StockInvestment, Integer> {
}
