package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.Transaction;
import org.hibernate.MappingException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IInvestmentService <Investment,ID>{
    Investment save(Investment entity);
    Investment add (Investment entity);
    Optional<Investment> findById(ID id);
    Investment update (ID id, Investment entity);
    List<Investment> findAll();
    void deleteById(ID id);
    void delete(Investment entity);
    public Investment AcceptInvestment(ID id);
    public Investment RefuseInvestment(ID id);
    public void Checkinvest();
    public void ReturnInvestment(Transaction transaction);
    Investment Invest(int owner_id, BigDecimal amount_invested, Integer investment_id,Integer project_id);
}
