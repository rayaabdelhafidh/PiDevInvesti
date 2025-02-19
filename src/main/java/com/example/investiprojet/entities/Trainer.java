package com.example.investiprojet.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainer extends User { // Hérite de User, donc l'ID est déjà présent


    @JsonProperty("specialization")
    private String specialization;
    @JsonProperty("experienceYears")
    private int experienceYears;
    @JsonProperty("certifications")
    private String certifications;
    @JsonProperty("photoUrl")
    private String photoUrl;
}
