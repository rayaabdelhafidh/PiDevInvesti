package com.example.investi.Entities;

import com.example.investi.Entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Admin extends User {

    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("dateCreation")
    private LocalDateTime dateCreation; // Date de création du compte
    @JsonProperty("active")
    private boolean active = true; // Compte actif ou non
    @JsonProperty("role")
    private String role;  // Rôle de l'utilisateur
}
