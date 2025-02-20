package com.example.investi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.investi.Entities.Sinister;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface SinisterRepository extends JpaRepository<Sinister, Long> {
    List<Sinister> findByContract_ContractId(Long contractId);
}
