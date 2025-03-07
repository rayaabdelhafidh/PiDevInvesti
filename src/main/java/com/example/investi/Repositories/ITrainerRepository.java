package com.example.investi.Repositories;

import com.example.investi.Entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITrainerRepository extends JpaRepository<Trainer,Long> {
}
