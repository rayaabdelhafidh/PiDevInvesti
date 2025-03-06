package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.Collateral;
import com.example.pidevinvesti.Entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICollateralRepo extends JpaRepository<Collateral, Long> {
    List<Collateral> findByUserIdUser(Long idUser);

}
