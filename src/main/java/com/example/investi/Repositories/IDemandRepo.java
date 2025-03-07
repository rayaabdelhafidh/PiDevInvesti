package com.example.investi.Repositories;

import com.example.investi.Entities.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IDemandRepo extends JpaRepository<Demand, Long> {
}
