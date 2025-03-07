package com.example.investi.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Data
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long DemandId;
    @JsonProperty("Amount")
    private float Amount ;
    @JsonProperty("Duration")
    private int Duration ;
    @JsonProperty("DemandDate")
    private Date DemandDate ;
    @JsonProperty("Status")
    @Enumerated(EnumType.STRING)
    private DemandStatus Status;
   @OneToOne
   private Loan loan ;

}
