package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.DemandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDemandRepo extends JpaRepository<Demand, Long> {

    int countByUserIdUserAndStatus(Long userId, DemandStatus status);

}
