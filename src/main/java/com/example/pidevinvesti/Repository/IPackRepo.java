package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.Demand;
import com.example.pidevinvesti.Entity.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPackRepo extends JpaRepository<Pack, Long> {

}
