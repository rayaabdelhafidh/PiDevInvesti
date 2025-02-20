package com.example.investi.Services;

import com.example.investi.Entities.Training;
import com.example.investi.Entities.TrainingCategory;
import com.example.investi.Entities.TrainingLevel;
import com.example.investi.Repositories.ITrainingService;
import com.example.investi.Repositories.TrainingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TrainingService implements ITrainingService {
    @Autowired
    private TrainingRepository trainingRepository;

    @Override
    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {

        return trainingRepository.findById(id);
    }




    @Override
    public Training updateTraining(Long id, Training training) {
        // Find the existing training by ID
        Optional<Training> existingTrainingOpt = trainingRepository.findById(id);

        if (existingTrainingOpt.isPresent()) {
            // Retrieve the existing training entity
            Training existingTraining = existingTrainingOpt.get();

            // Update the fields of the existing training entity
            existingTraining.setTitle(training.getTitle());
            existingTraining.setDescription(training.getDescription());
            existingTraining.setCategory(training.getCategory()); // Enums will work fine
            existingTraining.setDuration(training.getDuration());
            existingTraining.setLevel(training.getLevel()); // Enums will work fine

            // Save and return the updated training entity
            return trainingRepository.save(existingTraining);
        }
    return null;
    }



    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);

    }

    public List<Training> getTrainingsByCategory(String category) {
        try {
            TrainingCategory trainingCategory = TrainingCategory.valueOf(category.toUpperCase());
            return trainingRepository.findByCategory(trainingCategory);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category);
        }
    }

    public List<Training> getTrainingsByLevel(String level) {
        try {
            TrainingLevel trainingLevel = TrainingLevel.valueOf(level.toUpperCase());
            return trainingRepository.findByLevel(trainingLevel);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid level: " + level);
        }
    }
}
