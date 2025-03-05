package com.example.investi.Services;

import com.example.investi.Entities.StatusInvest;
import jakarta.mail.MessagingException;

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
