package com.example.investiprojet.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
