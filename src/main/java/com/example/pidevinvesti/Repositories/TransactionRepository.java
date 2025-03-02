package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Investment;
import com.example.pidevinvesti.Entities.Transaction;
import com.example.pidevinvesti.Entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findAllByAccountId(Long accountId);
    List <Transaction> findByInvestmentAndType(Investment investment, TransactionType type);
}
