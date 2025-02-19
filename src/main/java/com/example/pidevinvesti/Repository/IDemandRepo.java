package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IDemandRepo extends JpaRepository<Demand, Long> {
}
