package com.example.investi.Repositories;

import com.example.investi.Entities.Loan;
import com.example.investi.Entities.LoanStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoanRepo extends JpaRepository<Loan,Long> {

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.demand.user.id = :userId AND l.Status = :Status")
    boolean existsActiveLoanByUserId(@Param("userId") Long userId, @Param("Status") LoanStatut status);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.demand.user.id = :userId AND l.Status = :Status")
    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("Status") LoanStatut status);

}
