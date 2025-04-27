package com.example.investi.Repositories;


import com.example.investi.Entities.Collateral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICollateralRepo extends JpaRepository<Collateral, Long> {
    List<Collateral> findByUserId(Long idUser);

}
