package com.example.pidevinvesti.Repositories;

import com.example.pidevinvesti.Entities.Account;
import com.example.pidevinvesti.Entities.AccountType;
import com.example.pidevinvesti.Entities.Investor;
import com.example.pidevinvesti.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    // Query to find an account by clientId and accountType
    @Query("SELECT a FROM Account a WHERE a.client.id = :clientId AND a.accountType = :accountType")
    List<Account> findByClientIdAndAccountType(@Param("clientId") Long clientId, @Param("accountType") AccountType accountType);

    List<Account> findByClientId(Long clientId);
    Account findByProject(Project project);
    Account findByInvestor(Investor investor);
}
