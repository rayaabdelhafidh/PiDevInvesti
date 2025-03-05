package com.example.pidevinvesti.Services;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.Project;
import com.example.pidevinvesti.Entities.StatusInvest;
import com.example.pidevinvesti.Entities.Transaction;
import jakarta.mail.MessagingException;
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
    public void ReturnInvestment(ID id);
    Investment Invest(long owner_id, BigDecimal amount_invested,Integer project_id)throws MessagingException;
    List<Investment> findByStatus(StatusInvest status);


    }
