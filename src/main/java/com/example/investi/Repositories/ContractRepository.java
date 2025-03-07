package com.example.investi.Repositories;

import com.example.investi.Entities.Client;
import com.example.investi.Entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    long countByClient(Client client);
}
