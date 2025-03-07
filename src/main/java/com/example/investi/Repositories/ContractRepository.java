package com.example.investi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.investi.Entities.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
