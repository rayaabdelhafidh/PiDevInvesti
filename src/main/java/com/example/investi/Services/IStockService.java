package com.example.investi.Services;

import java.util.List;
import java.util.Optional;

public interface IStockService <StockInvestment,ID>{
    StockInvestment save(StockInvestment entity);
    StockInvestment add (StockInvestment entity);
    Optional<StockInvestment> findById(ID id);
    StockInvestment update (ID id, StockInvestment entity);
    List<StockInvestment> findAll();
    void deleteById(ID id);
    void delete(StockInvestment entity);
}
