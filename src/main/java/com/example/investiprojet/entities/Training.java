package com.example.investiprojet.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TrainingCategory category;
    private int duration; // in minutes

    @Enumerated(EnumType.STRING)
    private TrainingLevel level;

    @Enumerated(EnumType.STRING)
    private TrainingStatus status = TrainingStatus.PENDING;

    //@Column(updatable = false)
    //private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="trainer_id")
    @JsonProperty("trainer")
    private Trainer trainer;
}
