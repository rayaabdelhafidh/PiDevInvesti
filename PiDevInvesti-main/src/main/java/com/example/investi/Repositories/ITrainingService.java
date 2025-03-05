package com.example.investi.Repositories;



import com.example.investi.Entities.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Repository
public interface ITrainingService {
    Training createTraining(Training training);
    List<Training> getAllTrainings();
    Optional<Training> getTrainingById(Long id);
    List<Training> getTrainingsByCategory(String category);
    List<Training> getTrainingsByLevel(String level);
    Training updateTraining(Long id, Training training);
    void deleteTraining(Long id);
}

