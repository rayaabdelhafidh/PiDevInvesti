package com.example.investiprojet.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.example.investiprojet.entities.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;



@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Bonne stratégie pour une table par entité enfant
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; // ID unique pour toutes les entités héritant de User

        @JsonProperty("firstName")
        private String firstName;
        @JsonProperty("lastName")
        private String lastName;
        @JsonProperty("password")
        private String password;
        @JsonProperty("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime creationdate;
        @JsonProperty("email")
        private String email;
        @JsonProperty("adresse")
        private String adresse;
        @JsonProperty("phonenumber")
        private String phonenumber;
        @Enumerated(EnumType.STRING)
        private UserStatus statut; // Enumération pour éviter les erreurs de saisie
        private String role;







}
