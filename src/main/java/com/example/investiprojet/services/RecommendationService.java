package com.example.investiprojet.services;

import com.example.investiprojet.entities.Client;
import com.example.investiprojet.entities.Recommendation;
import com.example.investiprojet.entities.Training;
import com.example.investiprojet.repositories.ClientRepository;
import com.example.investiprojet.repositories.RecommendationRepository;
import com.example.investiprojet.repositories.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired

    private  ClientRepository clientRepository;
    @Autowired
    private  TrainingRepository trainingRepository;
    @Autowired
    private  RecommendationRepository recommendationRepository;



    /**
     * Générer des recommandations pour un client donné
     */
    public List<Training> getRecommendationsForClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return new ArrayList<>();

        // 3️⃣ Basé sur les formations populaires (filtrage collaboratif)
        List<Training> popularTrainings = trainingRepository.findMostPopularTrainings();

        // Combiner les recommandations et supprimer les doublons
        List<Training> recommendedTrainings = new ArrayList<>();

        recommendedTrainings.addAll(popularTrainings);

        return recommendedTrainings.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Sauvegarder une recommandation
     */
    public void saveRecommendation(Client client, Training training) {
        Recommendation recommendation = new Recommendation();
        recommendation.setClient(client);
        recommendation.setTraining(training);
        recommendationRepository.save(recommendation);
    }
}
