package tn.esprit.assurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.assurance.entity.Sinister;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface SinisterRepository extends JpaRepository<Sinister, Long> {
    List<Sinister> findByContract_ContractId(Long contractId);
}
