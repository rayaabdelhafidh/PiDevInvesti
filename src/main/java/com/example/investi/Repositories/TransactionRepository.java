package com.example.investi.Repositories;

import com.example.investi.Entities.Transaction;
import com.example.investi.Entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findAllByAccountId(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND (:startDate IS NULL OR t.transactionDate >= :startDate) AND (:endDate IS NULL OR t.transactionDate <= :endDate)")
    List<Transaction> findByAccountIdAndDates(@Param("accountId") Long accountId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Transaction> findByType(TransactionType transactionType);
}
