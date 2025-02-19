package com.example.investiprojet.repositories;

import com.example.investiprojet.entities.Training;
import com.example.investiprojet.entities.TrainingCategory;
import com.example.investiprojet.entities.TrainingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {

    List<Training> findByCategory(TrainingCategory category);
    List<Training> findByLevel(TrainingLevel level);
    }

