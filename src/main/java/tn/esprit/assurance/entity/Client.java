package tn.esprit.assurance.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

    public class Client {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonProperty("client_id")
        private Long clientId;


        @OneToMany(mappedBy = "client")
        private List<Contract> contracts;

    }


