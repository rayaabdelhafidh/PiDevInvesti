package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED) // Bonne stratégie pour une table par entité enfant
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
public class User {
        @Id
        @JsonIgnore()
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; // ID unique pour toutes les entités héritant de User

        @JsonProperty("firstName")
        private String firstName;
        @JsonProperty("lastName")
        private String lastName;
        @JsonProperty("password")
        private String password;
        @JsonProperty("creation-date'")
        private LocalDateTime creationdate;
        @JsonProperty("email")
        private String email;
        @JsonProperty("adresse")
        private String adresse;
        @JsonProperty("phonenumber")
        private String phonenumber;
        @JsonProperty("enabled")
        private boolean enabled;
}
