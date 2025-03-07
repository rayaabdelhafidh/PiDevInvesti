package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
        @JsonIgnore

        private List<Sinister> approvedSinisters;

        // Getters et Setters
    }


