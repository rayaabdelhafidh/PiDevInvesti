package com.example.investi.Repositories;

import com.example.investi.Entities.Compensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompensationRepository extends JpaRepository<Compensation, Long> {
}
