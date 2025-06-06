package com.example.investiprojet.services;

import com.example.investiprojet.entities.Training;
import com.example.investiprojet.entities.TrainingCategory;
import com.example.investiprojet.entities.TrainingLevel;
import com.example.investiprojet.entities.TrainingStatus;
import com.example.investiprojet.repositories.TrainingRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
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

    @Override

    public List<Training> getTrainingsByCategory(String category) {
        try {
            TrainingCategory trainingCategory = TrainingCategory.valueOf(category.toUpperCase());
            return trainingRepository.findByCategory(trainingCategory);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category);
        }
    }

    @Override
    public List<Training> getTrainingsByLevel(String level) {
        try {
            TrainingLevel trainingLevel = TrainingLevel.valueOf(level.toUpperCase());
            return trainingRepository.findByLevel(trainingLevel);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid level: " + level);
        }
    }

    @Override
    public List<Training> getPendingTrainings() {
        return trainingRepository.findByStatus(TrainingStatus.PENDING);
    }
    // Valider une formation
    @Override
    public void approveTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow();
        training.setStatus(TrainingStatus.APPROVED);
        trainingRepository.save(training);
    }

    // Rejeter une formation
    @Override
    public void rejectTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow();
        training.setStatus(TrainingStatus.REJECTED);
        trainingRepository.save(training);
    }

    @Override
    public List<Training> searchTrainings(String title, TrainingCategory category, TrainingLevel level, Integer maxDuration) {
        if (title != null) {
            return trainingRepository.findByTitleContainingIgnoreCase(title);
        } else if (category != null) {
            return trainingRepository.findByCategory(category);
        } else if (level != null) {
            return trainingRepository.findByLevel(level);
        } else if (maxDuration != null) {
            return trainingRepository.findByDurationLessThan(maxDuration);
        }
        return trainingRepository.findByStatus(TrainingStatus.APPROVED); // Par défaut, afficher les formations approuvées
    }



}
