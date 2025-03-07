package com.example.investi.Services;


import com.example.investi.Entities.Loan;

import java.util.List;

public interface ILoanService {
    Loan addLoan(Loan loan , Long demandId);

    Loan updateLoan(Loan loan);

    void deleteLoan(Long id);

    Loan getLoan(Long id);

    List<Loan> getAllLoans();
}
