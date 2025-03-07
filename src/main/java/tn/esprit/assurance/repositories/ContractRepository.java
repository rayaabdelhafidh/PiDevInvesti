package tn.esprit.assurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.assurance.entity.Client;
import tn.esprit.assurance.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    long countByClient(Client client);
}
