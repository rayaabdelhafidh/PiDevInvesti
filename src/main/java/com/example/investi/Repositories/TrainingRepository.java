package com.example.investi.Repositories;

import com.example.investi.Entities.Training;
import com.example.investi.Entities.TrainingCategory;
import com.example.investi.Entities.TrainingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {

    List<Training> findByCategory(TrainingCategory category);
    List<Training> findByLevel(TrainingLevel level);
    }

