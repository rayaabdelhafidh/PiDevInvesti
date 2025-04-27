package com.example.investi.Repositories;


import com.example.investi.Entities.Training;
import com.example.investi.Entities.TrainingCategory;
import com.example.investi.Entities.TrainingLevel;
import com.example.investi.Entities.TrainingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Long> {

    List<Training> findByCategory(TrainingCategory category);
    List<Training> findByLevel(TrainingLevel level);

    List<Training> findByStatus(TrainingStatus status);
    List<Training> findByTitleContainingIgnoreCase(String title);
    List<Training> findByDurationLessThan(int duration);


    // Trouver les formations les plus populaires (celles suivies le plus souvent)
    @Query("SELECT t FROM Training t JOIN Recommendation r ON t.id = r.training.id GROUP BY t.id ORDER BY COUNT(r.id) DESC")
    List<Training> findMostPopularTrainings();

    }

