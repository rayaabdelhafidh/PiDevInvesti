package com.example.investiprojet.entities;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private int experienceYears;
    private String certifications;
    private String photoUrl;

    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings = new ArrayList<>();



}

