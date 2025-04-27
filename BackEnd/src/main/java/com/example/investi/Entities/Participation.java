package com.example.investi.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private LocalDateTime participationDate;
    private String status; // Par exemple : "Inscrit", "Terminé", etc.


}
