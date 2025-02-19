package com.example.investiprojet.services;



import com.example.investiprojet.entities.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ITrainingService {
    Training createTraining(Training training);
    List<Training> getAllTrainings();
    Optional<Training> getTrainingById(Long id);
    List<Training> getTrainingsByCategory(String category);
    List<Training> getTrainingsByLevel(String level);
    Training updateTraining(Long id, Training training);
    void deleteTraining(Long id);
}

