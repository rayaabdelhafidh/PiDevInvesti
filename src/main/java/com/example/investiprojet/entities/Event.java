package com.example.investiprojet.entities;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int maxParticipants;
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @OneToMany(mappedBy = "event")
    private List<Participation> participations;
}

