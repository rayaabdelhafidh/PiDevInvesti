package com.example.investiprojet.entities;




import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Client extends User {


    @JsonProperty("dateOfBirth")
    private LocalDateTime dateOfBirth;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("profession")
    private String profession;
    @JsonProperty("prefferedLanguage")
    private String preferredLanguage;
    @JsonProperty("nationalId")
    private String nationalId;
    // New attributes for recommendations based on behavior
    @JsonProperty("financialGoals")
    private String financialGoals; // e.g., "saving for retirement"

    @JsonProperty("incomeLevel")
    private String incomeLevel; // e.g., "low", "medium", "high"

    @JsonProperty("financialExperience")
    private String financialExperience; // e.g., "beginner", "intermediate", "advanced"

    @JsonProperty("preferredTrainingCategory")
    private String preferredTrainingCategory; // e.g., "investing", "insurance"

    @JsonProperty("learningStyle")
    private String learningStyle; // e.g., "video", "reading", "interactive"

    @OneToMany(mappedBy = "client")
    private List<Participation> participations;

}

