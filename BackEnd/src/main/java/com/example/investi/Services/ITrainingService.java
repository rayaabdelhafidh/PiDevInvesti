package com.example.investi.Services;




import com.example.investi.Entities.Training;
import com.example.investi.Entities.TrainingCategory;
import com.example.investi.Entities.TrainingLevel;

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

    List<Training> getPendingTrainings();

    // Valider une formation
    void approveTraining(Long trainingId);

    // Rejeter une formation
    void rejectTraining(Long trainingId);

    List<Training> searchTrainings(String title, TrainingCategory category, TrainingLevel level, Integer maxDuration);
}

