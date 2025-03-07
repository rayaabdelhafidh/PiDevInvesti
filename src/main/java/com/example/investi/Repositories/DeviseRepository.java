package com.example.investi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.investi.Entities.Devise;


@Repository

public interface DeviseRepository extends JpaRepository<Devise,Integer> {
    Devise findBySymboleDevise(String deviseSource);
}
