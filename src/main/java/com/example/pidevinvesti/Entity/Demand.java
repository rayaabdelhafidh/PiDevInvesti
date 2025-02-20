package com.example.pidevinvesti.Entity;

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
    private float Amount ;
    private float IntrestRate ;
    private int Duration ;
    private Date DemandDate ;
    @Enumerated(EnumType.STRING)
    private DemandStatus Status;
   @OneToOne
   private Loan loan ;

}
