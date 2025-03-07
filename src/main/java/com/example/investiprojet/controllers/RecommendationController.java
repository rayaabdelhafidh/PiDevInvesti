package com.example.investiprojet.controllers;

import com.example.investiprojet.entities.Training;
import com.example.investiprojet.services.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired

    private  RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{clientId}")
    public List<Training> getRecommendations(@PathVariable Long clientId) {
        return recommendationService.getRecommendationsForClient(clientId);
    }
}

