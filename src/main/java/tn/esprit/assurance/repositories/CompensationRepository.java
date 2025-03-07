package tn.esprit.assurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.assurance.entity.Compensation;

@Repository
public interface CompensationRepository extends JpaRepository<Compensation, Long> {
}
