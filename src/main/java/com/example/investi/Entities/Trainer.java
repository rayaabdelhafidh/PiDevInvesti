package com.example.investi.Entities;

import com.example.investi.Entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;




import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


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
