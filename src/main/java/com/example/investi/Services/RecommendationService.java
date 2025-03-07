package com.example.investi.Services;


import com.example.investi.Entities.Client;
import com.example.investi.Entities.Recommendation;
import com.example.investi.Entities.Training;
import com.example.investi.Repositories.ClientRepository;
import com.example.investi.Repositories.RecommendationRepository;
import com.example.investi.Repositories.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired

    private ClientRepository clientRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private RecommendationRepository recommendationRepository;




    public List<Training> getRecommendationsForClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return new ArrayList<>();


        List<Training> popularTrainings = trainingRepository.findMostPopularTrainings();

        // Combiner les recommandations et supprimer les doublons
        List<Training> recommendedTrainings = new ArrayList<>();

        recommendedTrainings.addAll(popularTrainings);

        return recommendedTrainings.stream().distinct().collect(Collectors.toList());
    }


    public void saveRecommendation(Client client, Training training) {
        Recommendation recommendation = new Recommendation();
        recommendation.setClient(client);
        recommendation.setTraining(training);
        recommendationRepository.save(recommendation);
    }
}
