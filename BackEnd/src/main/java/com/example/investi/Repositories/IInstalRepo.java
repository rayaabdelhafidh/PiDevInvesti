package com.example.investi.Repositories;

import com.example.investi.Entities.Installement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInstalRepo extends JpaRepository<Installement,Long> {
}
