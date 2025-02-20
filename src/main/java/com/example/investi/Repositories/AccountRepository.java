package com.example.investi.Repositories;

import com.example.investi.Entities.Account;
import com.example.investi.Entities.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Query to find an account by clientId and accountType
    @Query("SELECT a FROM Account a WHERE a.client.idUser = :clientId AND a.accountType = :accountType")
    List<Account> findByClientIdAndAccountType(@Param("clientId") Long clientId, @Param("accountType") AccountType accountType);

    List<Account> findByClientIdUser(Long clientId);


}