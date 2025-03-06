package com.example.pidevinvesti.Repository;

import com.example.pidevinvesti.Entity.Installement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInstalRepo extends JpaRepository<Installement,Long> {
}
