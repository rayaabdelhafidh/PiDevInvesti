package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

    public class Client {
    public enum Sexe {
        Masculin,
        Feminin
    }
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonProperty("client_id")
        private Long clientId;
    @JsonProperty("dateOfBirth")
    private  LocalDate dateOfBirth;

        @JsonProperty("gender")
        @Enumerated(EnumType.STRING)
        private Sexe gender;
        @JsonProperty("profession")
        private String profession;
        @JsonProperty("joinDate")
        private LocalDate joinDate;
        @OneToMany(mappedBy = "client")
        private List<Contract> contracts;
        @JsonProperty("email")
        private String email;

    }


