package com.example.investi.Repositories;

import com.example.investi.Entities.Devise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface DeviseRepository extends JpaRepository<Devise,Integer> {
    Devise findBySymboleDevise(String deviseSource);
}
