package com.example.investi.Services;

import com.example.investi.Entities.Trainer;
import com.example.investi.Repositories.ITrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    @Autowired
    private ITrainerRepository tr;


    public Trainer AddTrainer(Trainer trainer) {
        return tr.save(trainer);
    }


    public Trainer UpdateTrainer(Trainer trainer) {
        // Check if the trainer exists in the database
        Optional<Trainer> existingTrainer = tr.findById(trainer.getId());
        if (existingTrainer.isEmpty()) {
            throw new IllegalArgumentException("Trainer not found with ID: " + trainer.getId());
        }

        // Update the fields conditionally
        Trainer updatedTrainer = existingTrainer.get();
        if (trainer.getFirstName() != null) {
            updatedTrainer.setFirstName(trainer.getFirstName());
        }
        if (trainer.getLastName() != null) {
            updatedTrainer.setLastName(trainer.getLastName());
        }
        if (trainer.getEmail() != null) {
            updatedTrainer.setEmail(trainer.getEmail());
        }
        if (trainer.getPassword() != null) {
            updatedTrainer.setPassword(trainer.getPassword());
        }
        if (trainer.getAdresse() != null) {
            updatedTrainer.setAdresse(trainer.getAdresse());
        }
        if (trainer.getPhonenumber() != null) {
            updatedTrainer.setPhonenumber(trainer.getPhonenumber());
        }
        if (trainer.getSpecialization() != null) {
            updatedTrainer.setSpecialization(trainer.getSpecialization());
        }
        if (trainer.getExperienceYears() != 0) {
            updatedTrainer.setExperienceYears(trainer.getExperienceYears());
        }
        if (trainer.getCertifications() != null) {
            updatedTrainer.setCertifications(trainer.getCertifications());
        }

        // Save the updated trainer
        return tr.save(updatedTrainer);
    }

    /**
     * Delete a trainer by ID.
     *
     * @param id The ID of the trainer to delete.
     */
    public void DeleteTrainer(Long id) {
        tr.deleteById(id);
    }

    /**
     * Get a trainer by ID.
     *
     * @param id The ID of the trainer.
     * @return The trainer with the specified ID.
     * @throws IllegalArgumentException If the trainer does not exist.
     */
    public Trainer GetTrainerById(Long id) {
        return tr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with ID: " + id));
    }

    /**
     * Get all trainers.
     *
     * @return A list of all trainers.
     */
    public List<Trainer> GetAllTrainers() {
        return tr.findAll();
    }
}