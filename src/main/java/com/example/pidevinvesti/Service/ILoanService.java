package com.example.pidevinvesti.Service;

import com.example.pidevinvesti.Entity.Loan;

import java.util.List;

public interface ILoanService {
    Loan addLoan(Loan loan , Long demandId);

    Loan updateLoan(Loan loan);

    void deleteLoan(Long id);

    Loan getLoan(Long id);

    List<Loan> getAllLoans();
}
