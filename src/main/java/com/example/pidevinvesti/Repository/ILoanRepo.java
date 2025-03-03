package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoanRepo extends JpaRepository<Loan,Long> {
}
