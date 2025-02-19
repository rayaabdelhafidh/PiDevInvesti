package tn.esprit.assurance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Insurer {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long insurerId;



        @OneToMany(mappedBy = "approvedBy")
        private List<Sinister> approvedSinisters;

        // Getters et Setters
    }


