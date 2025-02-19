package tn.esprit.assurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.assurance.entity.Client;
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
